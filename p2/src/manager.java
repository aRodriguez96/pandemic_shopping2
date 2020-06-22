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
    	System.out.println("The store is opened!");
    	int iS = 0; //local inStore variable to check how many people are in the store 
    	while(store.isStoreOpen) {
    		try {
				store.inStore_mutex.acquire();
				iS = store.inStore;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		store.inStore_mutex.release();
    		if( iS == 0) {
	    		System.out.println("Manager lets in the next group");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		}
    		} 		
    	 }
    	
    	
    	
    	
    	  
    }
}