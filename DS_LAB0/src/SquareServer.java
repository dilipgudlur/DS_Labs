  import java.net.*;
    import java.io.*;
    import java.util.Properties;
    import java.util.Enumeration;
  
    
    
 class MessagePasser {
    private String[] all_names = new String[10];
    private String[] all_ports = new String[10];
    private int num_hosts;
public MessagePasser(String configuration_filename, String local_name){
    
    try{
          File myFile = new File(configuration_filename);
          
          FileInputStream fis = new FileInputStream(myFile);
        Properties prop = new Properties();
           prop.load(fis);
           
           Enumeration en = prop.keys();
           Enumeration en_inner = prop.keys();
           boolean b,bp,b_is,b_ir,b_ks,b_kr;
           int loc=0;
           int count=0,count_port=0;
  
        String loc_name = "";
        String temp_name = "";
        String local_ip = "";
        String local_port = "";
        String is_action = "";    
        String ir_action = "";
        String ks_action = "";
        String kr_action = "";
        String is_action_id = "";  // id means the number like 1 of is.id1
        String ir_action_id = "";
        String ks_action_id = ""; // action means  drop of ks.kind1 drop
        String kr_action_id = "";
        
   
        en = prop.keys();
        while (en.hasMoreElements()) {
      String key = (String) en.nextElement();
      String val = (String)prop.getProperty(key);
          
      b = key.endsWith("ip");
        if(b==true) {
              loc = key.indexOf(".ip");
             // System.out.println("got a ip at location" + loc);
              loc_name = key.substring(5,loc);
             
                all_names[count]= loc_name;
                count++;
              System.out.println("local name is "+loc_name);
             
             // System.out.println(key + ":" + prop.get(key));
             local_ip = val;
               all_names[count]= local_ip;
               count++;
             System.out.println("ip for the local name is "+ local_ip);
             
              
       }
                            
      // bp =key.endsWith(loc_name+".port");
       bp =key.endsWith(".port");
             if(bp==true){
                loc = key.indexOf(".port");
                local_port = key.substring(5,loc);
                all_names[count_port]= local_port;
                count_port++;
             // System.out.println("got a ip at location" + loc);
             System.out.println("local port is "+local_port);
              local_port = val;
              all_ports[count_port]= local_port;
               count_port++;
              System.out.println("port for the local name is "+ local_port);
                }
      
      
        } // while loop ends

        
                 
     
        while (en_inner.hasMoreElements()) {
      String key = (String) en_inner.nextElement();
      String val = (String)prop.getProperty(key);
          
      b_is = key.startsWith("is.id");
      if(b_is==true) {
              loc = 5;
                is_action_id = key.substring(5,6);
              System.out.println("is action id is "+ is_action_id);
              is_action = val;
              System.out.println("is action "+ is_action);
      }
      
      
       b_ir = key.startsWith("ir.id");
      if(b_ir==true) {
              loc = 5;
                ir_action_id = key.substring(5,6);
              System.out.println("ir action is is "+ ir_action_id);
              ir_action = val;
              System.out.println("ir action "+ ir_action);
      }
      
       b_ks = key.startsWith("ks.kind");
      if(b_ks==true) {
              loc = 5;
                ks_action_id = key.substring(7,8);
              System.out.println("ks  kind is "+ ks_action_id);
              ks_action = val;
              System.out.println("ks kind "+ ks_action);
      }
      
      
      b_kr = key.startsWith("kr.kind");
      if(b_kr==true) {
              loc = 5;
                kr_action_id = key.substring(7,8);
              System.out.println("kr  kind is "+ kr_action_id);
              kr_action = val;
              System.out.println("kr kind "+ kr_action);
      }
      
      
    }
    
    num_hosts = count;
     System.out.println("Number of ips "+ count);
    System.out.println("Number of ports "+ count_port);
    for(int j=0;j<count-1;j=j+2){
        System.out.println("Ip & port for "+ all_names[j]+" is "+ "" +all_names[j+1] + "  "+all_ports[j+1]);
    }
    }// try
    
            catch (Exception e) {

                System.out.println("Exception: " + e);

            }
}
void send(Message message){
               // Socket s = new Socket(all_names[1],all_ports[1]);
               try {
               Socket s = new Socket("128.237.250.114",4565);
                System.out.println("Inside client");
   

                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                DataInputStream dis = new DataInputStream(s.getInputStream());
                dos.writeInt(6);

                int result = dis.readInt();
                System.out.println("The square of 6 is " + result);

                s.close();
               }
               catch (Exception e) {

                System.out.println("Exception: " + e);

            }
}
Message receive()  // need to make return type as Message
{ 
     try {
                ServerSocket ss = new ServerSocket(4679); 
                System.out.println("Inside server ");
                Socket clientSocket = ss.accept();
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                int result = dis.readInt();
                System.out.println("Inside server result is ");
                dos.writeInt(result);
                clientSocket.close();
                ss.close();

            }
            catch (Exception e) {

                System.out.println("Exception: " + e);

            }
}
}

 class Message {
	private String dest_name;
	private String msg_kind;
	private String msg_id;
	private Object msg_data;
	public Message(String dest, String kind, String id, Object data)
	{
	   dest_name = dest;
	   msg_kind = kind;
	   msg_id =id;
	   msg_data = data;
	}
}
    
 public class SquareServer {

  public static void main (String args[]) {
            
            try{
                String fileName = "lab0.config";
            MessagePasser first_one = new MessagePasser(fileName,"lab0");
            
            
    
    Object obj_for_msg = new Object();
    String kind_for_msg = "kind1";
    String id_for_msg = "1";
    String name_for_msg = "alice";
    Message msg_for_send = new Message(name_for_msg,kind_for_msg,id_for_msg,obj_for_msg);
              //  Socket s = new Socket("128.237.224.19",4645);
                
               msg_for_send= first_one.receive();
   
            }
            catch (Exception e) {

                System.out.println("Exception: " + e);

            }
       }

    }

