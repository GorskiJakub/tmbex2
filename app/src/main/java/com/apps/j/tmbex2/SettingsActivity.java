package com.apps.j.tmbex2;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;


import java.util.List;



public class SettingsActivity extends PreferenceActivity {
    /**
     * Określa kiedy pokazać ustawione UI.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // wyświetlenie  Action Bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {//Id odpowiadzialne za Home i Up button
            NavUtils.navigateUpFromSameTask(this);//pozwala użytkownikowi przekierować się na wyższy poziom struktur w aplikacji
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    //ustawienie prostego UI dla urządzenia, w zależności od konfiguracji
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // ustawienie 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference("city"));
        bindPreferenceSummaryToValue(findPreference("country"));
        bindPreferenceSummaryToValue(findPreference("units"));

    }


    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    /**
     * Sprawdzenie czy jest wyświetlane na dużym wyświetlaczu, np tablecie
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Określa, czy uproszczone ustawienia UI powinno być pokazane. Zwraca
     * true jeśli zostaje wymuszony {@link #ALWAYS_SIMPLE_PREFS}, albo urządzenie
     * nie posiada nowszych API jak {@link PreferenceFragment}, albo urządzenie nie posiada
     * dużego wyświetlacza. W tym przypadku zamieszcza urpszczony widok UI w 1 okienku.
     */
    private static boolean isSimplePreferences(Context context) {
        return ALWAYS_SIMPLE_PREFS
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }


    //utworzenie nagłówka
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        if (!isSimplePreferences(this)) {
            loadHeadersFromResource(R.xml.pref_headers, target);
        }
    }


     // Zmiana wartości preference dla listenera tak, aby update'ował nowe zestawienie

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // Sprawdza prawidłową wartość preferencji w liście preferencji
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // Ustawienie nowej wartości.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Przypisanie wartości do preference. W momencie gdy wartość jest zmieniana, jest update'owana
     * w celu wyświetlenie dobrej wartości. Wywoływane natychmiastowo po wywołaniu metody.
     * Rodzaj wyświetlanego formatu jest zależny od typu preference
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        //natychmiastowe wyzwolenie listener'a w zależności od wartości preference
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }


     //Fragment wyświetlający tylko główny preference.

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            bindPreferenceSummaryToValue(findPreference("city"));
            bindPreferenceSummaryToValue(findPreference("country"));
            bindPreferenceSummaryToValue(findPreference("units"));
        }
    }



}
