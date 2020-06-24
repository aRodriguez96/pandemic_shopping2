public class register implements Runnable {

    public String regID;
    public boolean isBusy = false;
    
    public Thread thread = Thread.currentThread();;

    public register(String regID) {
    	this.regID = regID;
        this.thread = new Thread(this);

    }

    public void start() {
        thread.start();
    }

    public void run() {
    	int cs = 0; //local customersSevered variable;
    	
    	while(cs < store.maxCustomers) {                        //start up the registers at the beginning of the day and wait for customers 
    		switch(Integer.parseInt(regID)) {
    			case 1: 
					try {
						store.reg1pay.acquire();
						store.reg1.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
    			case 2:
    				try {
    					store.reg2pay.acquire();
						store.reg2.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				break;
    			case 3:
    				try {
    					store.reg3pay.acquire();
						store.reg3.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				break;
    		}
    		try {
    			store.customersServed_mutex.acquire();          
    			cs = store.customersServed;
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		store.customersServed_mutex.release();
    		if(cs == store.maxCustomers) {         //when employee releases registers at end of day, checks to see if no more customers are left in store and closes down the register
    			msg("Register-"+regID+" closes for the day");
    			break;
    		}
    		msg("Register-"+regID+" is scanning groceries...");
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		switch(Integer.parseInt(regID)) {
				case 1: 
					store.reg1pay.release();
					break;
				case 2:
					store.reg2pay.release();
					break;
				case 3:
					store.reg3pay.release();
					break;
    		}
        	try {                                                                
    			store.customersServed_mutex.acquire();
    			cs = store.customersServed;                         
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    		store.customersServed_mutex.release();
    		
    	}
    }
    
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - store.time) + "] " + thread.getName() + ": " + m);
    }
}