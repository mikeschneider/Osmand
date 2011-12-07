package net.osmand.plus.views;

import net.osmand.plus.routing.RoutingHelper.TurnType;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.FloatMath;

public class TurnPathHelper {

	// draw path 96x96
	// 64x64
	public static void calcTurnPath(Path pathForTurn, TurnType turnType, Matrix transform) {
		if(turnType == null){
			return;
		}
		pathForTurn.reset();
		int ha = 72;
		int wa = 72;

		int th = 12; // thickness
		pathForTurn.moveTo(wa / 2, ha - 1);
		float sarrowL = 23; // side of arrow ?
		float harrowL = (float) Math.sqrt(2) * sarrowL; // hypotenuse of arrow
		float spartArrowL = (float) ((sarrowL - th / Math.sqrt(2)) / 2);
		float hpartArrowL = (float) (harrowL - th) / 2;

		if (TurnType.C.equals(turnType.getValue())) {
			int h = (int) (ha - hpartArrowL - 18);
			pathForTurn.rMoveTo(th / 2, 0);
			pathForTurn.rLineTo(0, -h);
			pathForTurn.rLineTo(hpartArrowL, 0);
			pathForTurn.rLineTo(-harrowL / 2, -harrowL / 2); // center
			pathForTurn.rLineTo(-harrowL / 2, harrowL / 2);
			pathForTurn.rLineTo(hpartArrowL, 0);
			pathForTurn.rLineTo(0, h);
		} else if (TurnType.TR.equals(turnType.getValue())|| TurnType.TL.equals(turnType.getValue())) {
			int b = TurnType.TR.equals(turnType.getValue())? 1 : -1;
			float quadShiftX = 22;
			float quadShiftY = 22;
			int wl = 10; // width
			int h = (int) (ha - quadShiftY - harrowL + hpartArrowL - 5);
			int sl = wl + th / 2;
			
			pathForTurn.rMoveTo(-b * sl, 0);
			pathForTurn.rLineTo(0, -h);
			pathForTurn.rQuadTo(0, -quadShiftY, b * quadShiftX, -quadShiftY);
			pathForTurn.rLineTo(b * wl, 0);
			
			pathForTurn.rLineTo(0, hpartArrowL);
			pathForTurn.rLineTo(b * harrowL / 2, -harrowL / 2); // center
			pathForTurn.rLineTo(-b * harrowL / 2, -harrowL / 2);
			pathForTurn.rLineTo(0, hpartArrowL);
			
			pathForTurn.rLineTo(-b * wl, 0);
			pathForTurn.rQuadTo(-b * (quadShiftX + th), 0, -b * (quadShiftX + th), quadShiftY + th);
			pathForTurn.rLineTo(0, h);
		} else if (TurnType.TSLR.equals(turnType.getValue()) || TurnType.TSLL.equals(turnType.getValue())) {
			int b = TurnType.TSLR.equals(turnType.getValue()) ? 1 : -1;
			int h = 24;
			int quadShiftY = 22;
			float quadShiftX = (float) (quadShiftY / (1 + Math.sqrt(2)));
			float nQuadShiftX = (sarrowL - 2 * spartArrowL) - quadShiftX - th;
			float nQuadShifty = quadShiftY + (sarrowL - 2 * spartArrowL);

			pathForTurn.rMoveTo(-b * 4, 0);
			pathForTurn.rLineTo(0, -h /* + partArrowL */);
			pathForTurn.rQuadTo(0, -quadShiftY + quadShiftX /*- partArrowL*/, b * quadShiftX, -quadShiftY /*- partArrowL*/);
			pathForTurn.rLineTo(b * spartArrowL, spartArrowL);
			pathForTurn.rLineTo(0, -sarrowL); // center
			pathForTurn.rLineTo(-b * sarrowL, 0);
			pathForTurn.rLineTo(b * spartArrowL, spartArrowL);
			pathForTurn.rQuadTo(b * nQuadShiftX, -nQuadShiftX, b * nQuadShiftX, nQuadShifty);
			pathForTurn.rLineTo(0, h);
		} else if (TurnType.TSHR.equals(turnType.getValue()) || TurnType.TSHL.equals(turnType.getValue())) {
			int b = TurnType.TSHR.equals(turnType.getValue()) ? 1 : -1;
			int h = 28;
			float quadShiftX = 22;
			int sh = 10;
			float quadShiftY = -(float) (quadShiftX / (1 + Math.sqrt(2)));
			float nQuadShiftX = -(sarrowL - 2 * spartArrowL) - quadShiftX - th;
			float nQuadShiftY = -quadShiftY + (sarrowL - 2 * spartArrowL);

			pathForTurn.rMoveTo(-b * sh, 0);
			pathForTurn.rLineTo(0, -h);
			pathForTurn.rQuadTo(0, -(quadShiftX - quadShiftY), b * quadShiftX, quadShiftY);
			pathForTurn.rLineTo(-b * spartArrowL, spartArrowL);
			pathForTurn.rLineTo(b * sarrowL, 0); // center
			pathForTurn.rLineTo(0, -sarrowL);
			pathForTurn.rLineTo(-b * spartArrowL, spartArrowL);
			pathForTurn.rCubicTo(b * nQuadShiftX / 2, nQuadShiftX / 2, b * nQuadShiftX, nQuadShiftX / 2, b * nQuadShiftX, nQuadShiftY);
			pathForTurn.rLineTo(0, h);
		} else if(TurnType.TU.equals(turnType.getValue()) || TurnType.TRU.equals(turnType.getValue())) {
			int h = 40;
			// right left
			int b = TurnType.TU.equals(turnType.getValue()) ? 1 : -1;
			float quadShiftX = 10; // 13
			float quadShiftY = 10; // 13
			int sm = 10;

			pathForTurn.rMoveTo(b * 28, 0);
			pathForTurn.rLineTo(0, -h);
			pathForTurn.rQuadTo(0, -(quadShiftY+th), -b * (quadShiftX+th), -(quadShiftY+th));
			pathForTurn.rQuadTo(-b * (quadShiftX+th), 0, -b * (quadShiftX+th), (quadShiftY+th));
			pathForTurn.rLineTo(0, sm);
			
			pathForTurn.rLineTo(-b * hpartArrowL, 0);
			pathForTurn.rLineTo(b * harrowL/2, harrowL/2); // center
			pathForTurn.rLineTo(b * harrowL/2, -harrowL/2);
			pathForTurn.rLineTo(-b  *hpartArrowL, 0);
			
			pathForTurn.rLineTo(0, -sm);
			pathForTurn.rQuadTo(0, -quadShiftX, b *quadShiftX, -quadShiftY);
			pathForTurn.rQuadTo(b * quadShiftX, 0, b * quadShiftX, quadShiftY);
			pathForTurn.rLineTo(0, h);
		} else if (turnType != null && turnType.isRoundAbout()) {
			float t = turnType.getTurnAngle();
			if (t >= 170 && t < 220) {
				t = 220;
			} else if (t > 160 && t < 170) {
				t = 160;
			}
			float sweepAngle = (t - 360) - 180;
			if (sweepAngle < -360) {
				sweepAngle += 360;
			}
			float r1 = ha / 3f;
			float r2 = 18f;
			float angleToRot = 0.3f;
			
			pathForTurn.moveTo(48, 48 + r1 + 8);
			pathForTurn.lineTo(48, 48 + r1);
			RectF r = new RectF(48 - r1, 48 - r1, 48 + r1, 48 + r1);
			pathForTurn.arcTo(r, 90, sweepAngle);
			float angleRad = (float) ((180 + sweepAngle)*Math.PI/180f);
			
			pathForTurn.lineTo(48 + (r1 + 4) * FloatMath.sin(angleRad), 48 - (r1 + 4) * FloatMath.cos(angleRad));
			pathForTurn.lineTo(48 + (r1 + 6) * FloatMath.sin(angleRad + angleToRot/2), 48 - (r1 + 6) * FloatMath.cos(angleRad + angleToRot/2));
			pathForTurn.lineTo(48 + (r1 + 12) * FloatMath.sin(angleRad - angleToRot/2), 48 - (r1 + 12) * FloatMath.cos(angleRad - angleToRot/2));
			pathForTurn.lineTo(48 + (r1 + 6) * FloatMath.sin(angleRad - 3*angleToRot/2), 48 - (r1 + 6) * FloatMath.cos(angleRad - 3*angleToRot/2));
			pathForTurn.lineTo(48 + (r1 + 4) * FloatMath.sin(angleRad - angleToRot), 48 - (r1 + 4) * FloatMath.cos(angleRad - angleToRot));
			pathForTurn.lineTo(48 + r2 * FloatMath.sin(angleRad - angleToRot), 48 - r2 * FloatMath.cos(angleRad - angleToRot));
			
			r.set(48 - r2, 48 - r2, 48 + r2, 48 + r2);
			pathForTurn.arcTo(r, 360 + sweepAngle + 90, -sweepAngle);
			pathForTurn.lineTo(40, 48 + r2);
			pathForTurn.lineTo(40, 48 + r1 + 8);
			pathForTurn.close();
		}
		pathForTurn.close();
		if(transform != null){
			pathForTurn.transform(transform);
		}
	}
	
	public static class RouteDrawable extends Drawable {
		Paint paintRouteDirection;
		Path p = new Path();
		Path dp = new Path();
		
		public RouteDrawable(){
			paintRouteDirection = new Paint();
			paintRouteDirection.setStyle(Style.FILL_AND_STROKE);
			paintRouteDirection.setColor(Color.rgb(100, 0, 255));
			paintRouteDirection.setAntiAlias(true);
		}
		

		@Override
		protected void onBoundsChange(Rect bounds) {
			Matrix m = new Matrix();
			m.setScale(bounds.width()/96f, bounds.height()/96f);
			p.transform(m, dp);
		}
		
		public void setRouteType(TurnType t){
			TurnPathHelper.calcTurnPath(p, t, null);
			onBoundsChange(getBounds());
		}

		@Override
		public void draw(Canvas canvas) {
			canvas.drawPath(dp, paintRouteDirection);
		}

		@Override
		public int getOpacity() {
			return 0;
		}

		@Override
		public void setAlpha(int alpha) {
			paintRouteDirection.setAlpha(alpha);
			
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			paintRouteDirection.setColorFilter(cf);
		}
		
	}


}