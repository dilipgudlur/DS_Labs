/*Authors : Venkat Raman Senapati(ECE) and Dilip Gudlur (INI)
 * Program to send and recieve stream of messages between multiple (4 presently) hosts
 * Process the messages ont he hosts based on parameters passed through user in a config file */    


	import java.net.*;
    import java.io.*;
    import java.util.Properties;
    import java.util.Enumeration;
    import java.util.*;
    
    
    /*Class Message Parser allows the communication between the hosts
     * It parses the configuration file to get the parameters of the file, 
     * creates and initializes corresponding buffers 
     * defines the send and receive functions for the hosts to communicate
     * */
    
    class MessagePasser {
    	private int is_ks_type;  // 1 means is, 0 means ks
    	private int ir_kr_type;  // 1 means ir, 0 means kr
    	private  ArrayList<Message> rec_messages;
	    private  ArrayList<Message> send_messages;
	    private String[] all_names = new String[10];
	    private String[] all_ports = new String[10];
	    private String[] ks_type = new String[10];
	    private String[] kr_type = new String[10];
	    private String[] is_type = new String[10];
	    private String[] ir_type = new String[10];
	    private String[] ks_action_type = new String[10];
	    private String[] kr_action_type = new String[10];
	    private String[] is_action_type = new String[10];
	    private String[] ir_action_type = new String[10];
	    private String[] msg_kind = {"1","2","3","4","5","6"};
	    private String[] msg_id = {"msgid1","msgid2","msgid3","msgid4","msgid5","msgid6"};
	    private Object[] msg_data = {12,34,56,78,89,90};
	    private int num_hosts;
    
	    public int is_present(){
	    	if (is_ks_type ==1)
	    		return 1;
	    	else
	    		return 0;
	    }
	    
	    public int ir_present(){
	    	if (ir_kr_type ==1)
	    		return 1;
	    	else
	    		return 0;
	    }
	    
	    public void print_is_action(){
	    	for (int j=0;j<
	    	is_type.length ;j++){
	    		System.out.println(is_type[j]);
            }
	    }
	    
	    public String get_is_action(String msg_type){
	    	int result=0;
	    	int flag=0;
	    	String tosend = "default";
	    	for (int j=0;j<is_type.length ;j++){
	    		if(msg_type!=null && is_type[j]!=null){
	    			if(is_type[j].equals(msg_type)){
	    				result =j;
	    				flag=1;
	    			}
	    		}
	    	}
        
	    	if(flag==1)
	    		return is_action_type[result];
	    	else
	    		return tosend;
        
	    }
        
	    public String get_ks_action(String msg_type){
	    	int result=0;
	    	int flag=0;
	    	String tosend = "default";
	    	for (int j=0;j<ks_type.length ;j++){
	    		if(msg_type!=null && ks_type[j]!=null){
	    			if(ks_type[j].equals(msg_type)){
	    				result =j;
	    				flag=1;
	    			}
	    		}
	    	}
	    	if(flag==1)
	    		return is_action_type[result];
	    	else
	    		return tosend;
       }
	
        public String get_ir_action(String msg_type){
	    	int result=0;
	    	int flag=0;
	    	String tosend = "default";
	    	for (int j=0;j<ir_type.length ;j++){
	    		if(msg_type!=null && ir_type[j]!=null){
	    			if(ir_type[j].equals(msg_type)){
	    				result =j;
	    				flag=1;
	    			}
	    		}
	    	}
            if(flag==1)
            	return ir_action_type[result];
            else
            	return tosend;
	    }
    
	    public String get_kr_action(String msg_type){
	    	int result=0;
	    	int flag=0;
	    	String tosend = "default";
	    	for (int j=0;j<kr_type.length ;j++){
	    		if(msg_type!=null && kr_type[j]!=null){
	    			if(kr_type[j].equals(msg_type)){
	    				result =j;
	    				flag=1;
	    			}
	    		}
	    	}
            if(flag==1)
            	return kr_action_type[result];
            else
            	return tosend;
	    }
	    
	    public int num_of_send_buffers(){
	    	return send_messages.size();
	    }
	    
	    public void add_rec_msg_to_buffer(Message msg){
	    	rec_messages.add(msg);
	    	// msg.disp_msg();
	    }
	    
	    public void add_send_msg_to_buffer(Message msg){
	    	send_messages.add(msg);
	    	//msg.disp_msg();
	    }
	    
	    public Message retrieve_msg_frm_send_buf(){
	    	Message msg = new Message();
	    	//msg = send_messages.get(0);
	    	msg = send_messages.remove(0);
	    	//msg.disp_msg();
	    	return msg;    
	    }
    
	    public Message read_msg_frm_send_buf(){
	    	Message msg = new Message();
	    	msg = send_messages.get(0);
	    	//msg = send_messages.remove(0);
	    	//  msg.disp_msg();
	    	return msg;    
	    }
       
	    public MessagePasser(String configuration_filename, String local_name){	//constructor
	    	try{
	    		rec_messages = new ArrayList<Message>();
	    		send_messages = new ArrayList<Message>();
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
		            	//System.out.println("local name is "+loc_name);
                      	// System.out.println(key + ":" + prop.get(key));
		            	local_ip = val;
		            	all_names[count]= local_ip;
		            	count++;
		            	//System.out.println("ip for the local name is "+ local_ip);
		            }
                    // bp =key.endsWith(loc_name+".port");
		            bp =key.endsWith(".port");
		            if(bp==true){
		            	loc = key.indexOf(".port");
		            	local_port = key.substring(5,loc);
		            	all_names[count_port]= local_port;
		            	count_port++;
		            	// System.out.println("got a ip at location" + loc);
		            	//    System.out.println("local port is "+local_port);
		            	local_port = val;
		            	all_ports[count_port]= local_port;
		            	count_port++;
		            	//System.out.println("port for the local name is "+ local_port);
		            }
		        } // while loop ends
                
		        int is_cnt=0,ir_cnt=0,ks_cnt=0,kr_cnt=0;
		        is_ks_type=0;
		        ir_kr_type=0;
		        while (en_inner.hasMoreElements()) {
		        	String key = (String) en_inner.nextElement();
		        	String val = (String)prop.getProperty(key);
		        	b_is = key.startsWith("is.id");
		        	if(b_is==true) {
		        		is_ks_type =1;
		        		loc = 5;
		        		is_action_id = key.substring(5,6);
		        		is_type[is_cnt]= is_action_id;
		        		is_action = val;
		        		is_action_type[is_cnt]= is_action;
		        		is_cnt++;
              
		        	}
		        	b_ir = key.startsWith("ir.id");
		        	if(b_ir==true) {
		        		ir_kr_type=1;
		        		loc = 5;
		        		ir_action_id = key.substring(5,6);
		        		ir_type[ir_cnt]= ir_action_id;
		        		//   System.out.println("ir action is is "+ ir_action_id);
		        		ir_action = val;
		        		ir_action_type[ir_cnt]= ir_action;
		        		ir_cnt++;
		        		//    System.out.println("ir action "+ ir_action);
		        	}
		        	b_ks = key.startsWith("ks.kind");
		        	if(b_ks==true) {
		        		loc = 5;
		        		ks_action_id = key.substring(7,8);
		                ks_type[ks_cnt]= ks_action_id;
		                // System.out.println("ks  kind is "+ ks_action_id);
		                ks_action = val;
		                ks_action_type[ks_cnt]=ks_action;
		                ks_cnt++;
		                //  System.out.println("ks kind "+ ks_action);
		        	}
		        	b_kr = key.startsWith("kr.kind");
		        	if(b_kr==true) {
		        		loc = 5;
		        		kr_action_id = key.substring(7,8);
		        		kr_type[kr_cnt]= kr_action_id;
		        		//    System.out.println("kr  kind is "+ kr_action_id);
		        		kr_action = val;
		        		kr_action_type[kr_cnt]=kr_action;
		        		kr_cnt++;
		        		//    System.out.println("kr kind "+ kr_action);
		        	}
                }
    		    num_hosts = count/2;
    		    System.out.println("Number of ips "+ count);
			    System.out.println("Number of ports "+ count_port);
			    for(int j=0;j<count-1;j=j+2){
			    	// System.out.println("Ip & port for "+ all_names[j]+" is "+ "" +all_names[j+1] + "  "+all_ports[j+1]);
			    } // here the total file has been parsed
			    
			    // create messages & add to send buffer now
			    for(int j=0;j< num_hosts;j=j+1){
			    	Message msg_for_buf = new Message(all_names[j*2],msg_kind[j],msg_id[j],msg_data[j]);
			    	this.add_send_msg_to_buffer(msg_for_buf);
			    	//  msg_for_buf.disp_msg();
			    }
    
	    	}// try block ends
    
            catch (Exception e) {
                System.out.println("Exception: " + e);
            }
	    }
	    
	    /*Function Send in Class Message Parser opens the connection on the sending host
	     * writes the message to output and closes the connection*/
	    
	    void send(Message message){
	    	// Socket s = new Socket(all_names[1],all_ports[1]);
	    	try {
	    		for(int j = 1;j<num_hosts*2;j=j+2){
	    			Socket s = new Socket(all_names[j],Integer.parseInt(all_ports[j]));
                    System.out.println("Inside client");
                    //    message.disp_msg();
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    PrintWriter writer = new PrintWriter(s.getOutputStream(),true);
                    writer.println(message.get_destname());
	                writer.println(message.get_msg_kind());
	                writer.println(message.get_msg_id());
	                writer.println(message.get_msg_data().toString());
	               s.close();
            	}
	    	}
            catch (Exception e) {
                System.out.println("Exception: " + e);
            }
	    }
	    
	    /* Function Receive in Class Message Parser,Opens socket on receiving host
	     * Retrieves the message components and closes the connection */
	    
	    Message receive(){
	    	Message msg_received = new Message();
	    	try {               
	            String dest_name_msg ="";
	            String dest_msg_kind ="";
	            String dest_msg_id ="";
	            String payload = "";
	            int cnt =0;
	            ServerSocket ss = new ServerSocket(2859); 
	            System.out.println("Inside server ");
	            Socket clientSocket = ss.accept();
	            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
	            //DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
	            BufferedReader dis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            String result;
	            while ((result = dis.readLine()) != null){
	            	if(cnt==0)
	            		dest_name_msg= result;
	            	else if(cnt==1)
	            		dest_msg_kind= result;
	            	else if(cnt==2)
	            		dest_msg_id= result;
	            	else
	            		payload = payload + result;
	            	cnt++;
                }
               msg_received = new Message(dest_name_msg,dest_msg_kind,dest_msg_id,payload);
               System.out.println("Inside server result is ");
               // dos.writeInt(result);
               clientSocket.close();
               ss.close();
	    	}
            catch (Exception e) {
                System.out.println("Exception: " + e);
            }
            return msg_received;
	    }
    }

    /*Class Message initializes the message components */
    class Message {
    	private String dest_name;
    	private String msg_kind;
		private String msg_id;
		private Object msg_data;
		public Message(String dest, String kind, String id, Object data){	//overloaded constructor
		   dest_name = dest;
		   msg_kind = kind;
		   msg_id =id;
		   msg_data = data;
		}
		
		public Message(){	//defautl constructor
		   dest_name = "";
		   msg_kind = "";
		   msg_id = "";
		   msg_data = null;
		}
		
		public int checkmessage_null(){ //checks for empty payload in message		   
		   if(msg_data == null)
			   return 1;
		   else
			   return 0;
		}

		public void disp_msg(){
		    System.out.println(" Dest name is " + dest_name);
		    System.out.println(" Dest name is " + msg_kind);
		    System.out.println(" Dest name is " + msg_id);
		    System.out.println(" Dest name is " + msg_data);
		}
		
		String get_destname(){	//gets the destination name
		    return dest_name;
		}
		
		String get_msg_kind(){	//gets the kind of message
		    return msg_kind;
		}
		
		String get_msg_id(){	//gets the message id
		    return msg_id;
		}
		
		Object get_msg_data(){	//gets the message payload
		    return msg_data;
		}
    }
    
    /*Class Send Thread to send the messages to the hosts
     * Instantiates the Message Class and sends the messages
     * Processes the messages based on the Config file parameters and performs
     * the specified action*/
    
    class Send_thread implements Runnable {
        MessagePasser first_one;
        
        public Send_thread()
        {}
        
        public Send_thread(String file){
        	String fileName = file ;//"lab0.config";
        	first_one = new MessagePasser(fileName,"lab0");
        }
        
        public void run(){
		    Integer int_obj = new Integer(5);
		    Object obj_for_msg = int_obj;
		    String kind_for_msg = "kind1";
		    String id_for_msg = "1";
		    String name_for_msg = "alice";
		    //  Message msg_for_send = new Message(name_for_msg,kind_for_msg,id_for_msg,obj_for_msg);
		    Message msg_for_send = new Message();
		    Message msg_for_send2 = new Message();
		    while(first_one.num_of_send_buffers()>0){
		    	msg_for_send = first_one.read_msg_frm_send_buf();
		    	String msg_action_type;
		    	String msg_kind1 = msg_for_send.get_msg_kind();
		    	if(first_one.is_present()==1)
		    		msg_action_type = first_one.get_is_action(msg_kind1);
		    	else
		    		msg_action_type = first_one.get_ks_action(msg_kind1);
    			System.out.println("The action to take for this " +msg_kind1 +" is " + msg_action_type);
    			if(msg_action_type.equals("delay")){
    				System.out.println("delaying");
    				msg_for_send = first_one.retrieve_msg_frm_send_buf();
    				msg_for_send2 = first_one.retrieve_msg_frm_send_buf();
				    first_one.send(msg_for_send2);
				    first_one.send(msg_for_send);
    			}
    			else if(msg_action_type.equals("duplicate")){
    				System.out.println("duplicating");
    				first_one.send(msg_for_send);
    				first_one.send(msg_for_send);
    				msg_for_send = first_one.retrieve_msg_frm_send_buf();
    			}
    			else if(msg_action_type.equals("drop")){
    				System.out.println("dropping");
    				msg_for_send = first_one.retrieve_msg_frm_send_buf();
    			}
    			else{
    				msg_for_send = first_one.retrieve_msg_frm_send_buf();
    				first_one.send(msg_for_send);
    			}
	    	}
        }
    }
    
    /*Class Receive Thread to receive the messages on the hosts
     * Instantiates the Message Class and recives the messages
     * Processes the messages based on the Config file parameters and performs
     * the specified action*/
    
    class Receive_thread implements Runnable {
        MessagePasser first_one;
        
        public Receive_thread(){
        	
        }
        public Receive_thread(String file){
            String fileName = file ;//"lab0.config";
            first_one = new MessagePasser(fileName,"lab0");
        }
        public void run(){
        	Message msg_for_receive = new Message();
            while(true){
            	msg_for_receive = first_one.receive();
            	if (msg_for_receive.checkmessage_null()==0){
            		String msg_action_type;
            		String msg_kind1 = msg_for_receive.get_msg_kind();
            		if(first_one.ir_present()==1)	//check for ID or KIND in Config file
            			msg_action_type = first_one.get_ir_action(msg_kind1);
            		else
            			msg_action_type = first_one.get_kr_action(msg_kind1);
            		System.out.println("The action to take for this " +msg_kind1 +" is " + msg_action_type);
            		if(msg_action_type.equals("delay")){	//delay the messages
            			System.out.println("delaying");
            			msg_for_receive.disp_msg();
            		}
            		else if(msg_action_type.equals("duplicate")){	//duplicate the messages
            			System.out.println("duplicating");
            			msg_for_receive.disp_msg();
            			first_one.add_rec_msg_to_buffer(msg_for_receive);
            			first_one.add_rec_msg_to_buffer(msg_for_receive);
            		}
            		else if(msg_action_type.equals("drop")){	//drop the messages
            			msg_for_receive.disp_msg();
            			System.out.println("dropping");
            		}
            		else	//receive the messages
            		{
            			System.out.println("defaulting");
            			msg_for_receive.disp_msg();
            			first_one.add_rec_msg_to_buffer(msg_for_receive);
            		}
            	}
            }
        }
    }
    
    
    /* Class Messenger contains main which invokes Message Parser class
     invokes two threads of send and receive functionality to perform the
     process of sending and receiving the message between the hosts*/
    
    public class Messenger {
    	public static void main (String args[]) {
    	String filename = null;
    	try{
    			if(args.length > 0){
    				//for (String s: args){
    		           // System.out.println(s);
    				filename = args[0];
    			
    			}
    		}
    		catch (Exception e) {
            	System.out.println("Exception: " + e);
            }
    		try{
                Send_thread sth =  new Send_thread(filename);
                Receive_thread rth = new Receive_thread(filename);
                Thread a = new Thread(sth);
                Thread b = new Thread(rth);
                //b.start();
                a.start();
                while(true){
                	//a.sleep(10);
                	//b.sleep(1);
                }
    		}
            catch (Exception e) {
            	System.out.println("Exception: " + e);
            }
    	}
    }