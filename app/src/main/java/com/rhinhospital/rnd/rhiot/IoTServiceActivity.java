package com.rhinhospital.rnd.rhiot;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rhinhospital.rnd.rhiot.Model.BloodPressure;
import com.rhinhospital.rnd.rhiot.RetrofitAPI.RetrofitService;
import com.rhinhospital.rnd.rhiot.util.RhinLog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IoTServiceActivity extends Activity {
    private final static String TAG = "IoTServiceActivity";

    // used to request fine location permission
    private final static int REQUEST_FINE_LOCATION= 2;
    private TextView mTxtSystolic, mTxtDiastolic, mTxtPulseRate, mTxtTime;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private ScanFilter  mScanFilter;
    private List<ScanFilter> mFilters;
    private BluetoothGatt mGatt;
    private Button mBtnScan = null;
    private WebView mWebView = null;
    private Button mBtnTransmission = null;
    BluetoothManager mBluetoothManager;
    private static final int MEASURMENTS_MESSAGE_HANDLER = 0;
    private static final int DEVICEFOUND_MESSAGE_HANDLER = 1;
    SendMessageHandler mMessageHandler = null;

    private TextView mTxtDevice,mTxtMac,mTxtStatus;
    public String mStrTime = null;
    public String mStrSys = null;
    public String mStrDia = null;
    public String mStrPulse = null;
    public String mStrDeviceName = null;

    // Blood pressure
    static public final UUID UUID_SERVICE_BP = UUID.fromString("00001810-0000-1000-8000-00805F9B34FB");
    static public final UUID UUID_CHAR_BP_MEASUREMENT = UUID.fromString("00002A35-0000-1000-8000-00805F9B34FB");
    protected static final UUID CLIENT_CHARACTERISTIC_CONFIG= UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public final static String MAC_ADDR= "B0:49:5F:02:B7:02";
    private BluetoothGattCharacteristic mCharBpMeasure;
    private boolean mParied = false;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_service);
        RhinLog.print("-- IoT Service Activity Start --");
        RhinLog.print("-- View Init --");
        view_init();
        RhinLog.print("-- Ble Init --");
        ble_init();
        //blue_scan();
        //mTxtStatus.setText(" Device Scan Ready ");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGatt != null) {
            mGatt.close();
            mGatt = null;
        }
    }

    private void webViewInit() {
        /*
        String url = "https://youtu.be/0yOiYHEp7nw";

        mWebView = findViewById(R.id.iot_webview);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        mWebView.loadUrl(url);
        */
    }

    private  void view_init() {

        mMessageHandler = new SendMessageHandler();
        mBtnScan = (Button)findViewById(R.id.iot_device_scan);
        if( mBtnScan != null ) {
            mBtnScan.setOnClickListener(mBtnClickListener);
        }
        mBtnTransmission = (Button)findViewById(R.id.iot_transmission);
        if( mBtnTransmission != null ) {
            mBtnTransmission.setOnClickListener(mBtnClickListener);
        }
//        mTxtStatus = (TextView)findViewById(R.id.iot_status_log);
//        mTxtDevice = (TextView)findViewById(R.id.iot_get_device_name);
//        mTxtMac = (TextView)findViewById(R.id.iot_get_device_mac);
        mTxtSystolic = (TextView)findViewById(R.id.systolic);
        mTxtDiastolic = (TextView)findViewById(R.id.diastolic);
        mTxtPulseRate = (TextView)findViewById(R.id.pulse_rate);
        mTxtTime = (TextView)findViewById(R.id.timestamp);

    }

    /*
    Request BLE enable
    */
    private void requestEnableBLE() {
        Intent ble_enable_intent= new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
        startActivityForResult( ble_enable_intent, REQUEST_ENABLE_BT );
    }

    /*
    Request Fine Location permission
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestLocationPermission() {
        requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION );
    }

    // Ble Device Scan Seeting & Start
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void blue_scan(){
        RhinLog.print("-- Ble Scab Start--");
        //mTxtStatus.setText(" BlueTooth Scan Start");
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            requestEnableBLE();
        }
        // check if location permission
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestLocationPermission();
//            return;
//        }
        if (Build.VERSION.SDK_INT >= 21) {
            //filters = new ArrayList<ScanFilter>();
            // 필터 설정.. UUID 서비스만 스캔할건지.. MAC Address만 스캔할건지 옵션을 설정한다.
            mFilters = new ArrayList<ScanFilter>();
//            mFilters = new ArrayList<>();
//            ScanFilter filter = new ScanFilter.Builder()
//                    .setDeviceAddress(MAC_ADDR)
//                    .build();
//            mFilters.add(filter);

//                List<UUID> serviceUUIDs = new ArrayList<>();
//                for (UUID serviceUuid : serviceUUIDs) {
//                    ParcelUuid parcelUuid = new ParcelUuid(serviceUuid);
//                    ScanFilter filter = new ScanFilter.Builder()
//                            .setServiceUuid(parcelUuid)
//                            .build();
//                    mFilters.add(filter);
//                }

            RhinLog.print("-- Ble Mode Setting --");
            // BLE 저전력 모드로 셋팅.
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
        }
        RhinLog.print("-- Start Scan Ble Device --");
        scanLeDevice(true);
    }

    /**
     * 측정 결과 전송
     */
    private void measurementsDataTransmission() {
        Call<BloodPressure> bloodPressureTransmission = RetrofitService.getInstance().getService().setBloodPressureMeasure("0","00001", Integer.parseInt(mStrSys), Integer.parseInt(mStrDia), Integer.parseInt(mStrPulse), Long.parseLong(mStrTime));
        bloodPressureTransmission.enqueue(new Callback<BloodPressure>() {
            @Override
            public void onResponse(Call<BloodPressure> call, Response<BloodPressure> response) {
                Log.d(TAG, "onResponse");
                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<BloodPressure> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    Button.OnClickListener mBtnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.iot_device_scan:
                    blue_scan();
                    break;
                case R.id.iot_transmission:
                    measurementsDataTransmission();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    private void ble_init() {
        // Handle Init
        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        RhinLog.print("-- Ble Manager Set --");
        // Ble Manager Set
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        // Set Ble Adapter
        mBluetoothAdapter = mBluetoothManager.getAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //1번 :: 스켄하면 결과는 mScanCallback(onScanResult),mLeScanCallback(onLeScan) 에 나올거고..
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                //mTxtStatus.setText(" BlueTooth Start Scan !");
                RhinLog.print("-- Start Scan Ble Device Set Callback--");
                mLEScanner.startScan(mFilters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                if(mLEScanner != null)
                    mLEScanner.stopScan(mScanCallback);
            }
        }
    }


    //2번 ::
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            RhinLog.print("--onScanResult ---" );
            if(mParied == true) return;
            RhinLog.print("--callbackType" + String.valueOf(callbackType));
            RhinLog.print("--result" + result.toString());
            // get scanned device
            BluetoothDevice btDevice = result.getDevice();
            // get scanned device MAC address

            String strDeviceName = null;
            strDeviceName = btDevice.getName();
            String strDeviceMac= btDevice.getAddress();

            RhinLog.print("Device = " + btDevice.getName() + ", [" + strDeviceMac + "]");

            if(strDeviceName != null) {
                mTxtDevice.setText(strDeviceName);
            }
            mTxtMac.setText(strDeviceMac);

//            if(btDevice.getAddress().contains(MAC_ADDR)) {
//                connectToDevice(btDevice);
//            }

            //if(strDeviceName != null && (mParied == false) ){
            if(strDeviceName != null ){
                if (strDeviceName.contains("BLEsmart") ||strDeviceName.contains("BLESmart") ) {
                    RhinLog.print("-- Find Ble Device Device Name --" + strDeviceName);
                    RhinLog.print("-- Find Ble Device Device Mac --" + strDeviceMac);
                    mParied = true;
                    mStrDeviceName = strDeviceName;

                    RhinLog.print("Find Omron BP Device !!");

                    if(  mMessageHandler != null ) {
                        Message sendMsg = mMessageHandler.obtainMessage();
                        sendMsg.what = DEVICEFOUND_MESSAGE_HANDLER;
                        mMessageHandler.sendMessage(sendMsg);
                    }
                    connectToDevice(btDevice);
                }
            }
            // if(!mTxtDevice.getText().toString().isEmpty()) {
            //if (btDevice.getName().contains("BLESmart_00000")) {//3번 :: 오므론 디바이스 네임이 맞으면.. gatt connect 한다.
//                if(mTxtDevice.getText().toString().contains("BLESmart_00000")) {
//                    connectToDevice(btDevice);
//                    scanLeDevice(false);
//                }
            //}
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
                RhinLog.print("ScanResult - Results" + sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
            RhinLog.print("Scan Failed" + "Error Code: " + errorCode);
        }

    };

    //2번 ::
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.i("onLeScan", device.toString());
                            RhinLog.print(device.toString());
                            //Log.i("Device = " + device.getName() + ", [" + device.getAddress() + "]");
                            if(device.getName().contains("HEM-9200T"))//3번 :: 오므론 디바이스 네임이 맞으면.. gatt connect 한다.
                                connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
        if (mGatt == null) {
            RhinLog.print("-- Connect To Device --");
            mGatt = device.connectGatt(this, false, gattCallback);
            //4번 :: 커넥트가 잘되면 gattCallback에 onConnectionStateChange로 콜백이 온다.
            RhinLog.print("-- ScanLeDevice Stop --");
        }
        scanLeDevice(false);// will stop after first device detection
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            RhinLog.print("Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    RhinLog.print("BlueTooth GattCalback STATE_CONNECTED");
                    //5번 :: 연결이 성공적이면 서비스를 찾아본다.
                    //그러면 아래 onServicesDiscovered로 콜백이 온다.
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    RhinLog.print("BlueTooth GattCalback STATE_DISCONNECTED");
                    mParied = false;
                    //scanLeDevice(true);
                    if (mGatt == null) {
                        break;
                    }
                    mGatt.close();
                    mGatt = null;
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            RhinLog.print("onServicesDiscovered" + services.toString());
//            gatt.readCharacteristic(services.get(1).getCharacteristics().get
//                    (0));
            //6번 :: 서비스 디스커버리가 되면 쭉 살펴보면서 혈압 서비스를 찾고 거기서 혈압 측정 케렉터리스틱을 인디케이드 한다..
            //그리고 기다리면 측정이 완료되거나 지가 데이터 보내고 싶을때 데이터를 보내 onCharacteristicChanged로 콜백이 와..
            for(BluetoothGattService service : gatt.getServices()){
                if(UUID_SERVICE_BP.equals(service.getUuid())){
                    mCharBpMeasure = service.getCharacteristic(UUID_CHAR_BP_MEASUREMENT);
                    setCharIndication(mCharBpMeasure);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            RhinLog.print("onCharacteristicRead" + characteristic.toString());
            gatt.disconnect();
        }

        //측정결과 콜백 오는곳..
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(UUID_CHAR_BP_MEASUREMENT.equals(characteristic.getUuid())){
                processBp(characteristic);
            }

        }
    };
    private boolean processBp(BluetoothGattCharacteristic characteristic){
        if(UUID_CHAR_BP_MEASUREMENT.equals(characteristic.getUuid()) == false) return false;

        int offset = 0;
        int flags = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);
        int unit = flags & 0x01;
        int timeStampPresent = flags & 0x02;
        int pulseRatePresent = flags & 0x04;

        float systolic = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, offset);
        float diastolic = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, offset + 2);
        float meanArterialPressure = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, offset + 4);
        offset += 6;

        Calendar calendar = Calendar.getInstance();
        String timeString = "null";
        if(timeStampPresent > 0){
            int year = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset);
            offset += 2;
            int month = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);
            int day = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);
            int hour = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);
            int min = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);
            int sec = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset++);

            calendar.set(year, month - 1, day, hour, min, sec);
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            timeString = format.format(calendar.getTime());
        }

        float pulseRate = 0;
        if(pulseRatePresent > 0){
            pulseRate = characteristic.getFloatValue(BluetoothGattCharacteristic.FORMAT_SFLOAT, offset);
        }

        String unitString = "mmHg";
        if(unit > 0) unitString = "kPa";

        mStrTime = timeString;
        mStrDia = String.format(" %.1f",diastolic);
        mStrSys = String.format(" %.1f",systolic);
        mStrPulse = String.format(" %.1f",pulseRate);

        String msg = String.format("[%s] systolic = %.1f %s, diastolic = %.1f %s, mean = %.1f %s, pulse = %.1f"
                , timeString
                , systolic, unitString
                , diastolic, unitString
                , meanArterialPressure, unitString
                , pulseRate);
        if(  mMessageHandler != null ) {
            Message sendMsg = mMessageHandler.obtainMessage();
            sendMsg.what = MEASURMENTS_MESSAGE_HANDLER;
            mMessageHandler.sendMessage(sendMsg);
        }

        RhinLog.print(msg);
        return true;
    }

    private  void setCharIndication(final BluetoothGattCharacteristic characteristic){
        //log.n();
        boolean status;
        //status = mGatt.setCharacteristicNotification(characteristic, true);
        final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG);
        if(descriptor != null){
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
            status = mGatt.setCharacteristicNotification(characteristic, true);
            RhinLog.print("Status: " + status);
        }else{
            //log.e(characteristic.getUuid() + " : descriptor error");
        }
    }
    public class SendMessageHandler extends Handler {
        private String strTime,strSys,strDia,strPulse;
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MEASURMENTS_MESSAGE_HANDLER:
                    mTxtTime.setText(mStrTime); // 측정시간
                    mTxtSystolic.setText(mStrSys); // 수축기 혈압
                    mTxtDiastolic.setText(mStrDia); // 이완기 혈압
                    mTxtPulseRate.setText(mStrPulse); // 맥박수
                    break;
                case DEVICEFOUND_MESSAGE_HANDLER:
                    //mTxtStatus.setText(" Find Ble Device = " + mStrDeviceName);
                    break;
            }
        }
    }
}
