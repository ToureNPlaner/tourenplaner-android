package de.uni.stuttgart.informatik.ToureNPlaner.Net;

import de.uni.stuttgart.informatik.ToureNPlaner.Data.ServerInfo;
import junit.framework.TestCase;
import org.json.JSONObject;

public class ServerInfoTest extends TestCase {
    final static String INFO = "{\"sslport\":8081,\"servertype\":\"public\",\"algorithms\":[{\"constraints\":{\"sourceIsTarget\":false,\"minPoints\":2},\"pointconstraints\":null,\"urlsuffix\":\"sp\",\"name\":\"Shortest Path\",\"version\":1}],\"version\":0.1}";
    final JSONObject object;

    public ServerInfoTest() throws Exception {
        object = new JSONObject(INFO);
    }

    public void testParse() throws Exception {
        System.out.println(object.toString());
        ServerInfo info = ServerInfo.parse(object);

        assertEquals(ServerInfo.ServerType.PUBLIC, info.getServerType());
        assertEquals(8081,info.getSslPort());
        assertEquals("0.1",info.getVersion());
    }
}
