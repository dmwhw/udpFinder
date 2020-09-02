package Test;
 
import com.example.amyli.my.base.RequestSearchData;
import com.example.amyli.my.server.SearchServer;

 
/**
 * Created by amyli on 2017/2/13.
 */

public class Server {
    private static final byte DEVICE_TYPE_NAME_21 = 0x21;
    private static final byte DEVICE_TYPE_ROOM_22 = 0x22;


    public static void init() {
        SearchServer server = new SearchServer(1024) {

            @Override
            public void printLog(String log) {
                System.out.println("server:" + log);
                 
            }

            @Override
            public void onReceiveSearchReq(RequestSearchData data) {
                System.out.println("server:" + data);
            }
        };
        server.init();
    }

    public static void main(String[] args) {
        init();
    }
}
