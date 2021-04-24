package com.undertheriver.sgsg.foler.domain;

public enum FolderColor {
	BLUE("#2DA5D7"),
	GREEN("#00A09B"),
	ORANGE("#F99A42"),
	PURPLE("#9F69DB"),
	RED("#E64632");

	String colorCode;

	FolderColor(String colorCode) {
		this.colorCode = colorCode;
	}

	public static FolderColor getNextColor(int folderRowSize) {
		FolderColor[] values = FolderColor.values();
		int nextColorOrdinary = folderRowSize % FolderColor.values().length;
		return values[nextColorOrdinary];
	}
}
