package com.ivancot.playlistmaker

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsIntentsTest {
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun emailOpensDraftWithRequiredSubjectAndBody() = withSettings {
        device.findObject(By.text("Написать разработчикам")).click()
        Intents.intended(allOf(
            hasAction(Intent.ACTION_SENDTO),
            hasExtra(Intent.EXTRA_EMAIL, equalTo(arrayOf("20072005i@gmail.com"))),
            hasExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker"),
            hasExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!"),
        ))
    }

    @Test
    fun agreementOpensEducationalOffer() = withSettings {
        device.findObject(By.text("Пользовательское соглашение")).click()
        Intents.intended(allOf(hasAction(Intent.ACTION_VIEW), hasData("https://yandex.ru/legal/practicum_offer/ru/")))
    }

    @Test
    fun shareOpensChooserContainingPlainTextMessage() = withSettings {
        device.findObject(By.text("Поделиться приложением")).click()
        Intents.intended(allOf(
            hasAction(Intent.ACTION_CHOOSER),
            hasExtra(equalTo(Intent.EXTRA_INTENT), allOf(
                hasAction(Intent.ACTION_SEND), hasType("text/plain"),
                hasExtra(Intent.EXTRA_TEXT, "Попробуйте Playlist Maker! https://github.com/IvanCot/playlistmaker"),
            )),
        ))
    }

    private fun withSettings(check: () -> Unit) {
        ActivityScenario.launch(MainActivity::class.java).use {
            device.wait(Until.findObject(By.text("Настройки")), 5_000).click()
            checkNotNull(device.wait(Until.findObject(By.text("Написать разработчикам")), 5_000))
            Intents.init()
            try {
                // Intercept external apps: tests never send mail or publish a share.
                Intents.intending(anyIntent()).respondWith(ActivityResult(Activity.RESULT_OK, null))
                check()
            } finally { Intents.release() }
        }
    }
}
