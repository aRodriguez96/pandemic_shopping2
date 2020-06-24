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
			store.elderlyRegisterAvailable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	//sends waiting customers to registers while the customers served is less than max customers
    	while(cs < store.maxCustomers) {
    		//System.out.println(store.registerAvailable.getQueueLength());
    		if(store.registerAvailable.getQueueLength() > 0) {
    			if(store.register[0].isBusy == false || store.register[1].isBusy == false) { //if register for regular customer becomes available, send customer to register
    				store.registerAvailable.release();
    				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			}
    		}
    		 
    		if(store.elderlyRegisterAvailable.getQueueLength() > 0) {        //if register for elderly is available, send elderly to register
    			if(store.register[2].isBusy == false) {
    				store.elderlyRegisterAvailable.release();
    				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
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
    	try {                                               //start closing the store
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	msg("Employee begins closing the store...");
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	store.reg1.release();                //start closing down the register threads
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	store.reg2.release();
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	store.reg3.release();
    	try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
      	try {
    			Thread.sleep(5000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
      	try {
			store.isStoreOpen_mutex.acquire(); 
			store.isStoreOpen = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
      	store.isStoreOpen_mutex.release();
     	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}                                            //store is closed
      	msg("Employee closes the store and gets to the parking lot to save the distressed customers!");
      	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
      	for(int i = 0; i < store.maxCustomers; i++) {                       //let customers go home from parking lot
      		store.parkingLotLineSemaphores[i].release();
      		try {
    			Thread.sleep(1000);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
      	}
      	msg("Employee leaves the parking lot");
    }
    
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - store.time) + "] " + thread.getName() + ": " + m);
    }
}