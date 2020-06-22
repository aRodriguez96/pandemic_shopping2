public class employee implements Runnable {

    public boolean empNeeded = true;
    
    public Thread thread = Thread.currentThread();

    public employee() {
    	
        this.thread = new Thread(this);

    }

    public void start() {
        thread.start();
    }

    public void run() {
    	//lock the registers
    	int cs = 0;
    	try {
			store.registerAvailable.acquire();
			store.registerAvailable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	//sends waiting customers to registers
    	while(cs < store.maxCustomers) {
    		//System.out.println(store.registerAvailable.getQueueLength());
    		if(store.registerAvailable.getQueueLength() > 0) {
    			if(store.register[0].isBusy == false || store.register[1].isBusy == false) {
    				store.registerAvailable.release();
    				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
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
    
    
    
    
    
    
    
    
    
    
    
}