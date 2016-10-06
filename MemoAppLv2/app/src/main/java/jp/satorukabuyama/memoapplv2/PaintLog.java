package jp.satorukabuyama.memoapplv2;

import android.graphics.Path;

/**
 * 描画履歴
 */
public class PaintLog {
  Path mPath;
  int mColor;
  public PaintLog(Path path,int color) {
    mPath = path;
    mColor = color;
  }
  public int getColor() {
    return mColor;
  }
  public Path getPath() {
    return mPath;
  }
}
