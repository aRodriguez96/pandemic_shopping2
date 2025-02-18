public class manager implements Runnable {
    
	public Thread thread = Thread.currentThread();;

    public manager() {
        this.thread = new Thread(this);

    }

    public void start() {
        thread.start();
    }

    public void run() {
    	//locks the 6 spots in the store to let the customers group up
    	for(int i = 1; i<=6; i++) {
    		try {
				store.outsideLine.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	//waits to open store
    	 try {
			store.openStore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	msg("The store is opened!");
    	int iS = 0; //local inStore variable to check how many people are in the store 
    	boolean iO = true; //local is store open to know when the store closes.
    	
    	while(iO) { //while store is open
    		try {
				store.inStore_mutex.acquire();
				iS = store.inStore;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		store.inStore_mutex.release();
    		if( iS == 0 && store.customersServed < store.maxCustomers) {                          //when store empties, send in next group
	    		msg("Manager lets in the next group");
	    		for(int i = 1; i <=6; i++) {
	    			store.outsideLine.release();
	    			try {
						store.inStore_mutex.acquire();
						store.inStore++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    			store.inStore_mutex.release();
	    			//simple synchronization for the print statements
	    			try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
    		}
    		try {
				store.isStoreOpen_mutex.acquire();
				iO = store.isStoreOpen;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		store.isStoreOpen_mutex.release();
    	 }
    	 msg("Manager leaves the store");  //done for the day
    }
    
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - store.time) + "] " + thread.getName() + ": " + m);
    }
}