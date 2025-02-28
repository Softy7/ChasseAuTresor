package com.example.Chasse;


import android.content.Intent;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.GrantPermissionRule;
import com.example.Chasse.Activities.ChoiceConnectActivity;
import com.example.Chasse.Activities.Connect.LoginActivity;
import com.example.Chasse.Activities.Connect.Registractivity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.GrantPermissionRule;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ChoiceConnectActivityTest {


    //@Rule
    //public ActivityScenarioRule<ChoiceConnectActivity> activityScenarioRule = new ActivityScenarioRule<>(ChoiceConnectActivity.class);
    //https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/rules?hl=fr

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(

    );

    @Test
    public void testActivityChoiceConnectActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChoiceConnectActivity.class);
        // ajout d'un intent pour désactiver les animations, car ils bloquent les tests
        intent.putExtra("test_mode", true);
        try (ActivityScenario<ChoiceConnectActivity> scenario = ActivityScenario.launch(intent)){
            scenario.onActivity(activity -> {
                Log.d("TEST", "onActivity");
                // Permet de tester que l'activité est bien ouverte
                Assert.assertNotNull(activity);
            });
        }
    }

    @Test
    public void testActivityChoiceConnectActivityButtonLogin() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChoiceConnectActivity.class);
        intent.putExtra("test_mode", true);
        Intents.init(); // de espresso, qui permet tester l'UI sur Android
        try (ActivityScenario<ChoiceConnectActivity> scenario = ActivityScenario.launch(intent)){
            onView(withId(R.id.connect)).perform(click()); // Click sur le bouton Se Connecter
            intended(hasComponent(LoginActivity.class.getName())); // Vérifie si l'activity est LoginActivity
        } finally {
            Intents.release();
        }
    }

    @Test
    public void testActivityChoiceConnectActivityButtonRegister() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChoiceConnectActivity.class);
        intent.putExtra("test_mode", true);
        Intents.init();
        try (ActivityScenario<ChoiceConnectActivity> scenario = ActivityScenario.launch(intent)){
            onView(withId(R.id.registrering)).perform(click());
            intended(hasComponent(Registractivity.class.getName()));
        } finally {
            Intents.release();
        }
    }

}


