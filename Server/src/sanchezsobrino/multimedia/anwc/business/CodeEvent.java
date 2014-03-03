package sanchezsobrino.multimedia.anwc.business;

import java.util.HashMap;
import java.util.Map;

public enum CodeEvent {
	UNKNOWN(-1), ARRIBA(0), ABAJO(1), START_PRESENTATION(2), STOP_PRESENTATION(3), QUIT(4), IZQUIERDA(5), DERECHA(6);

	private int code;

	private CodeEvent(int c) {
		code = c;
	}

	private static final Map<Integer, CodeEvent> intToTypeMap = new HashMap<Integer, CodeEvent>();
	static {
		for (CodeEvent type : CodeEvent.values()) {
			intToTypeMap.put(type.code, type);
		}
	}

	public static CodeEvent fromInt(int i) {
		CodeEvent type = intToTypeMap.get(Integer.valueOf(i));
		if (type == null)
			return CodeEvent.UNKNOWN;
		return type;
	}
	
	public static int fromCodeEvent(CodeEvent event) {
		return event.code;
	}
}