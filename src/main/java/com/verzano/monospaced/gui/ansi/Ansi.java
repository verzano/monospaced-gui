package com.verzano.monospaced.gui.ansi;

public class Ansi {
  private Ansi() {}

  public static final char ESC = '\u001b';
  // Movement
  public static final String SET_POSITION = ESC + "[%d;%dH";
  public static final String MOVE_UP = ESC + "[%dA";
  public static final String MOVE_DOWN = ESC + "[%dB";
  public static final String MOVE_RIGHT = ESC + "[%C";
  public static final String MOVE_LEFT = ESC + "[%D";
  // Cursor Visibility
  public static final String HIDE_CURSOR = ESC + "[?25l";
  public static final String SHOW_CURSOR = ESC + "[?25h";
}
