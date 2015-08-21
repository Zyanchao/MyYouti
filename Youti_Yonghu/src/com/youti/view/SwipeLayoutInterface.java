package com.youti.view;

import com.youti.view.SwipeLayout.Status;

public interface SwipeLayoutInterface {

	Status getCurrentStatus();
	
	void close();
	
	void open();
}
