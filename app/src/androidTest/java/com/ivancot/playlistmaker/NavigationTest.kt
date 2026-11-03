package com.ivancot.playlistmaker

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Test
    fun querySurvivesRecreationAndCanBeCleared() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openScreen("Поиск")
            val query = device.wait(Until.findObject(By.desc("Поисковый запрос")), TIMEOUT)
            assertNotNull(query)
            query.text = "Кино"
            assertTrue(device.wait(Until.hasObject(By.text("Кино")), TIMEOUT))
            scenario.recreate()
            assertTrue(device.wait(Until.hasObject(By.text("Кино")), TIMEOUT))
            val clear = device.wait(Until.findObject(By.desc("Очистить поисковый запрос")), TIMEOUT)
            assertNotNull(clear)
            clear.click()
            assertTrue(device.wait(Until.gone(By.text("Кино")), TIMEOUT))
            assertTrue(device.wait(Until.gone(By.desc("Очистить поисковый запрос")), TIMEOUT))
            assertTrue(device.hasObject(By.text("Поиск")))
        }
    }

    @Test
    fun searchOpensAndSystemBackReturnsHome() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openScreen("Поиск")
            device.pressBack()
            assertTrue(device.wait(Until.hasObject(By.text("Playlist Maker")), TIMEOUT))
        }
    }

    @Test
    fun settingsOpensAndToolbarBackReturnsHome() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openScreen("Настройки")
            device.findObject(By.desc("Назад")).click()
            assertTrue(device.wait(Until.hasObject(By.text("Playlist Maker")), TIMEOUT))
        }
    }

    private fun openScreen(title: String) {
        val button = device.wait(Until.findObject(By.text(title)), TIMEOUT)
        assertNotNull("Main menu button is visible", button)
        button.click()
        assertTrue(device.wait(Until.hasObject(By.desc("Назад")), TIMEOUT))
        assertTrue(device.hasObject(By.text(title)))
        assertTrue(device.wait(Until.gone(By.text("Playlist Maker")), TIMEOUT))
    }

    private companion object {
        const val TIMEOUT = 5_000L
    }
}
