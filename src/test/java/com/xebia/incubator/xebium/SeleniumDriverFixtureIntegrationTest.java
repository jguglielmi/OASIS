package com.xebia.incubator.xebium;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;

import org.junit.Ignore;
import org.junit.Test;

public class SeleniumDriverFixtureIntegrationTest {

	@Test
	@Ignore
	public void testDefaultTest() {
		//		|script|selenium driver fixture|
		final SeleniumDriverFixture fixture = new SeleniumDriverFixture();
		//		| ensure | start browser | firefox | on url | http://www.google.com |
		fixture.startBrowserOnUrl("firefox", "http://www.google.com");
		//		| ensure | do | open | on | / |
		assertTrue(fixture.doOn("open", "/"));
		//		| ensure | do | type | on | q | with | xebium is the new test solution |
		assertTrue(fixture.doOnWith("type", "q", "blah"));
		//		| stop browser |
		fixture.stopBrowser();
	}

	@Test
	@Ignore
	public void testVerifyCommands() {
		// !!! Make sure FitNesse is running on localhost:8000

		//		|script|selenium driver fixture|
		final SeleniumDriverFixture fixture = new SeleniumDriverFixture();
		//		| start browser | firefox | on url | http://localhost:${FITNESSE_PORT}/ |
		fixture.startBrowserOnUrl("firefox", "http://localhost:8000");
		//		| ensure | do | open | on | !-/FitNesse.ProjectXebium.ExampleSuite.TableFixture-! |
		assertTrue(fixture.doOn("open", "/FitNesse.ProjectXebium.ExampleSuite.TableFixture"));
		//		| ensure | do | verifyTitle | on | !-FitNesse.ProjectXebium.ExampleSuite.TableFixture-! |
		assertTrue(fixture.doOn("verifyTitle", "FitNesse.ProjectXebium.ExampleSuite.TableFixture"));

		//		| ensure | do | verifyTextPresent | on | Test |
		assertTrue(fixture.doOn("verifyTextPresent", "Test"));
		//		| ensure | do | verifyText | on | //tr[1]/td[2] | with | selenium driver fixture |
		assertTrue(fixture.doOnWith("verifyText", "//tr[1]/td[2]", "selenium driver fixture"));
		//		| stop browser |
		fixture.stopBrowser();
	}

	@Test
	@Ignore
	public void loadFirefoxProfile() {
		SeleniumDriverFixture fixture = new SeleniumDriverFixture();
		fixture.loadCustomBrowserPreferencesFromFile("firefoxexample.json");
		fixture.startBrowserOnUrl("firefox", "http://localhost:8000");
		fixture.stopBrowser();
	}
	
	@Test
	@Ignore
	public void shouldFindWebElementInsideFrame(){
		
		final SeleniumDriverFixture fixture = new SeleniumDriverFixture();
		//		| start browser | firefox | on url | http://localhost:${FITNESSE_PORT}/ |
		fixture.startBrowserOnUrl("firefox", "http://localhost:8000");
		assertTrue(fixture.doOn("open", "/files/testHtml/iFrameTest1.html"));

		assertTrue(fixture.searchAllFrames("//input[@value='fill city']") != null);
		assertTrue(fixture.searchAllFrames("//input[@value='fill email']") != null);
		assertTrue(fixture.doOn("waitForVisible", "//input[@value='fill email']"));
		
		fixture.stopBrowser();
		//WebElement elem = seleniumDriverFixture.searchAllFrames("//*[id=verwijderen]");
		//assertTrue(elem != null);
	}

}
