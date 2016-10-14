///src/<your-package>/BDayWidgetProvider.java
public class BDayWidgetProvider extends AppWidgetProvider
{
   private static final String tag = "BDayWidgetProvider";

   public void onUpdate(Context context,
                     AppWidgetManager appWidgetManager,
                     int[] appWidgetIds) {
      final int N = appWidgetIds.length;
      for (int i=0; i<N; i++)
      {
         int appWidgetId = appWidgetIds[i];
         updateAppWidget(context, appWidgetManager, appWidgetId);
      }
   }

   public void onDeleted(Context context, int[] appWidgetIds) {
      final int N = appWidgetIds.length;
      for (int i=0; i<N; i++) {
         BDayWidgetModel.removePrefs(context, appWidgetIds[i]);
      }
   }

   @Override
   public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();
      if (AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)) {
         Bundle extras = intent.getExtras();
         final int appWidgetId = extras.getInt
                  (AppWidgetManager.EXTRA_APPWIDGET_ID,
                  AppWidgetManager.INVALID_APPWIDGET_ID);
         if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            this.onDeleted(context, new int[] { appWidgetId });
         }
      }
      else {
         super.onReceive(context, intent);
      }
   }

   public void onEnabled(Context context) {
      BDayWidgetModel.clearAllPreferences(context);
      PackageManager pm = context.getPackageManager();
      pm.setComponentEnabledSetting(
            new ComponentName("com.ai.android.BDayWidget",
                           ".BDayWidgetProvider"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP);
   }

   public void onDisabled(Context context) {
      BDayWidgetModel.clearAllPreferences(context);
      PackageManager pm = context.getPackageManager();
      pm.setComponentEnabledSetting(
         new ComponentName("com.ai.android.BDayWidget",
                        ".BDayWidgetProvider"),
         PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
         PackageManager.DONT_KILL_APP);
   }

   private void updateAppWidget(Context context,
                           AppWidgetManager appWidgetManager,
                           int appWidgetId) {
      BDayWidgetModel bwm = BDayWidgetModel.retrieveModel(context, appWidgetId);
      if (bwm == null) {
         return;
      }
      ConfigureBDayWidgetActivity
         .updateAppWidget(context, appWidgetManager, bwm);
   }
}
