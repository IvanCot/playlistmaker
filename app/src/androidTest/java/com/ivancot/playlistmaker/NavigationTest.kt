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
import java.io.File
import com.ivancot.playlistmaker.data.di.repositoryModule
import com.ivancot.playlistmaker.domain.api.TracksRepository
import com.ivancot.playlistmaker.domain.model.Track
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.io.IOException
import android.graphics.HardwareRenderer
import android.os.Build
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @Before
    fun enableScreenshotRendering() {
        if (Build.VERSION.SDK_INT >= 33) HardwareRenderer.setDrawingEnabled(true)
    }

    @Test
    fun failedSearchDisplaysErrorAndCanBeRetried() {
        var attempts = 0
        val failingModule = module {
            factory<TracksRepository> {
                object : TracksRepository {
                    override suspend fun searchTracksContains(expression: String): List<Track> {
                        if (++attempts == 1) throw IOException("Simulated unavailable source")
                        return listOf(Track(1, "Yesterday", "The Beatles", "02:05"))
                    }
                }
            }
        }
        loadKoinModules(failingModule)
        try {
            ActivityScenario.launch(MainActivity::class.java).use {
                openScreen("Поиск")
                queryField().text = "Yesterday"
                device.findObject(By.desc("Найти треки")).click()
                assertTrue(device.wait(Until.hasObject(By.text("Ошибка при поиске")), TIMEOUT))
                saveScreenshot("search-error")
                device.findObject(By.desc("Найти треки")).click()
                assertTrue(device.wait(Until.hasObject(By.text("The Beatles")), TIMEOUT))
                assertTrue(device.wait(Until.gone(By.text("Ошибка при поиске")), TIMEOUT))
            }
        } finally {
            unloadKoinModules(failingModule)
            loadKoinModules(repositoryModule)
        }
    }

    @Test
    fun initialHintAndEmptyResultsAreDisplayed() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openScreen("Поиск")
            assertTrue(device.wait(Until.hasObject(By.text("Введите строку для поиска")), TIMEOUT))
            saveScreenshot("search-initial")
            queryField().text = "no such track 123"
            device.findObject(By.desc("Найти треки")).click()
            assertTrue(device.wait(Until.hasObject(By.text("Ничего не найдено")), 10_000))
            saveScreenshot("search-empty")
            device.findObject(By.desc("Очистить поисковый запрос")).click()
            assertTrue(device.wait(Until.hasObject(By.text("Введите строку для поиска")), TIMEOUT))
            assertTrue(device.wait(Until.gone(By.text("Ничего не найдено")), TIMEOUT))
        }
    }

    @Test
    fun searchShowsResultsAndOpensTrackDetails() {
        ActivityScenario.launch(MainActivity::class.java).use {
            openScreen("Поиск")
            queryField().text = "Yesterday"
            device.wait(Until.findObject(By.desc("Найти треки")), TIMEOUT).click()
            val result = device.wait(Until.findObject(By.text("Yesterday (Remastered 2009)")), 10_000)
            assertNotNull(result)
            saveScreenshot("search-results")
            result.click()
            assertTrue(device.wait(Until.hasObject(By.text("О треке")), TIMEOUT))
            assertTrue(device.hasObject(By.text("The Beatles")))
            assertTrue(device.hasObject(By.text("Длительность: 02:05")))
            saveScreenshot("track-details")
            device.pressBack()
            assertTrue(device.wait(Until.hasObject(By.text("Yesterday (Remastered 2009)")), TIMEOUT))
            device.findObject(By.desc("Очистить поисковый запрос")).click()
            assertTrue(device.wait(Until.gone(By.text("Yesterday (Remastered 2009)")), TIMEOUT))
        }
    }

    @Test
    fun querySurvivesRecreationAndCanBeCleared() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            openScreen("Поиск")
            val query = queryField()
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
            saveScreenshot("settings")
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

    private fun saveScreenshot(name: String) {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val directory = File(checkNotNull(context.getExternalFilesDir(null)), "review-screenshots")
        check(directory.isDirectory || directory.mkdirs())
        assertTrue(device.takeScreenshot(File(directory, "$name.png")))
    }

    private fun queryField() = checkNotNull(device.wait(Until.findObject(By.clazz("android.widget.EditText")), TIMEOUT))

    private companion object {
        const val TIMEOUT = 5_000L
    }
}
