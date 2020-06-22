public class register implements Runnable {

    public String regID;
    public String customerServing;
    public boolean isBusy = false;
   // public boolean regNeeded = true;
    
    public Thread thread = Thread.currentThread();;

    public register(String regID) {
    	this.regID = regID;
        this.thread = new Thread(this);

    }

    public void start() {
        thread.start();
    }

    public void run() {
    	while(true) {
    		switch(Integer.parseInt(regID)) {
    			case 1: 
					try {
						store.reg1pay.acquire();
						store.reg1.acquire();
						System.out.println("reg1 ACTIVE");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
    			case 2:
    				try {
    					store.reg2pay.acquire();
						store.reg2.acquire();
						System.out.println("reg2 ACTIVE");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    				break;
    		}
    		System.out.println("Register-"+regID+" is scanning groceries...");
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
    		}
    		
    	}		
    }
}