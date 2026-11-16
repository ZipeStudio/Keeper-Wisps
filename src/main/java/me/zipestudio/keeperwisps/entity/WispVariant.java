package me.zipestudio.keeperwisps.entity;

import java.util.Locale;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum WispVariant {

	BLUE(0x84D4FD),
	ORANGE(0xFDD284),
	GREEN(0x8CFD84);

	private static final int BLUE_MIN_ITEMS = 6;

	private final int color;

	WispVariant(int color) {
		this.color = color;
	}

    public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public static WispVariant byId(int id) {
		WispVariant[] values = values();
		return id >= 0 && id < values.length ? values[id] : GREEN;
	}

	@Nullable
	public static WispVariant byName(String name) {
		for (WispVariant variant : values()) {
			if (variant.getSerializedName().equalsIgnoreCase(name)) {
				return variant;
			}
		}
		return null;
	}

	public static WispVariant byItemCount(int count) {
		if (count <= 0) {
			return GREEN;
		}
		return count >= BLUE_MIN_ITEMS ? BLUE : ORANGE;
	}

}
