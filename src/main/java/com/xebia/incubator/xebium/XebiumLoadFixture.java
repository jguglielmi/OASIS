
	package com.xebia.incubator.xebium;

import com.google.common.base.Supplier;
import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;
import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebDriverCommandProcessor;
import com.thoughtworks.selenium.webdriven.WebDriverCommandProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static com.xebia.incubator.xebium.FitNesseUtil.*;
import static org.apache.commons.lang.StringUtils.join;

	/**
	 * Main fixture. Starts a browser session and execute commands.
	 */
	public class XebiumLoadFixture {
		private static final Logger LOG = LoggerFactory.getLogger(XebiumLoadFixture.class);

	    private DefaultWebDriverSupplier defaultWebDriverSupplier = new DefaultWebDriverSupplier();

		private static final String ALIAS_PREFIX = "%";

		private CommandProcessor commandProcessor;

		// in milliseconds
		private long timeout = 60000;

		private long stepDelay = 0;

		private long pollDelay = 100;

		private boolean stopBrowserOnAssertion = true;

		private ScreenCapture screenCapture = new ScreenCapture();

		private LocatorCheck locatorCheck;

		private Map<String, String> aliases = new HashMap<String, String>();
		
		public int count = 0;
		
		ArrayList<WebDriver> driverArray = new ArrayList<WebDriver>();
		ArrayList<XebiumLoadFixture> driverFixtureArray = new ArrayList<XebiumLoadFixture>();
				
		public XebiumLoadFixture() {
			super();
		}

	    private WebDriver defaultWebDriverInstance() {
	      return defaultWebDriverSupplier.newWebDriver();
	    }

	    private CommandProcessor startWebDriverCommandProcessor(String browserUrl, WebDriver webDriver) {
			browserUrl = removeAnchorTag(browserUrl);
			return new WebDriverCommandProcessor(browserUrl, webDriver);
		}

	    /**
	     * Configure the custom Firefox preferences (javascript) file on the webdriver factory.
	     *
	     * @param filename
	     */
	    public void loadCustomBrowserPreferencesFromFile(String filename) {
	        defaultWebDriverSupplier.setCustomProfilePreferencesFile(new File(filename));
	    }

		/**
	     * Configure the custom Firefox profiledirectory on the webdriver factory.
	     *
		 * @param directory
		 */
		public void loadFirefoxProfileFromDirectory(String directory) {
	        defaultWebDriverSupplier.setCustomProfilePreferencesFile(new File(directory));
		}

		/**
		 * @param browser Name of the browser, as accepted by the DefaultWebDriverSupplier.
		 */
		private void setBrowser(String browser) {
	        defaultWebDriverSupplier.setBrowser(browser);
		}

	    /**
	     * <p><code>
	     * | start xebium load fixture on url | <i>http://localhost</i> |
	     * </code></p>
	     *
	     * @param browserUrl
	     */

	    public void startXebiumLoadFixtureOnUrl(final String browserUrl) throws InterruptedException {
	    	for(int i = 0; i < driverArray.size(); i++){
	    		XebiumLoadFixture xebiumLoadFixture = new XebiumLoadFixture();
	    		driverFixtureArray.add(xebiumLoadFixture);
	    		Thread.sleep(count);
	    		xebiumLoadFixture.startDriverOnUrl(driverArray.get(i), browserUrl);
		        LOG.debug("Started command processor for driver " + driverArray.get(i));
	    	}
	    }
	    
	    /**
	     * <p><code>
	     * | start driver | $Driver | on url | <i>http://localhost</i> |
	     * </code></p>
	     * @param webDriver
	     * @param browserUrl
	     */
	    public void startDriverOnUrl(final WebDriver webDriver, final String browserUrl) {
	        setCommandProcessor(startWebDriverCommandProcessor(browserUrl, webDriver));
	        setTimeoutOnSelenium();
	    }
	    
	    
	    /**
		 * <p><code>
		 * | set count to | 500 |
		 * </code></p>
		 *
		 * <p>Set the ramp up period for threads.</p>
		 *
		 * @param ramp-up timeout in milliseconds (ms)
		 */
	    
	    public void setCountTo(int count){
	    	this.count=count;
			LOG.info("(ramp-up period: " + count + "ms)");
	    }

		void setCommandProcessor(CommandProcessor commandProcessor) {
			this.commandProcessor=commandProcessor;
			screenCapture.setCommandProcessor(commandProcessor);
			locatorCheck = new LocatorCheck(commandProcessor);
			LOG.info("Started new command processor (ramp-up period: " + count + "ms, timeout: " + timeout + "ms, step delay: " + stepDelay + "ms, poll interval: " + pollDelay + "ms)");
		}

		/**
		 * <p><code>
		 * | set timeout to | 500 |
		 * </code></p>
		 *
		 * <p>Set the timeout, both local and on the running selenium server.</p>
		 *
		 * @param timeout Timeout in milliseconds (ms)
		 */
		public void setTimeoutTo(long timeout) {
			this.timeout = timeout;
			if (commandProcessor != null) {
				setTimeoutOnSelenium();
			}
		}

		/**
		 * <p><code>
		 * | set timeout to | 500 | seconds |
		 * </code></p>
		 *
		 * <p>Set the timeout, both local and on the running selenium server.</p>
		 *
		 * @param timeout Timeout in seconds
		 */
		public void setTimeoutToSeconds(long timeout) {
			setTimeoutTo(timeout * 1000);
		}

		/**
		 * Set the default timeout on the selenium instance.
		 */
		private void setTimeoutOnSelenium() {
			executeCommand("setTimeout", new String[] { "" + this.timeout });
	        WebDriver.Timeouts timeouts = getWebDriver().manage().timeouts();
	        timeouts.setScriptTimeout(this.timeout, TimeUnit.MILLISECONDS);
	        timeouts.pageLoadTimeout(this.timeout, TimeUnit.MILLISECONDS);
		}

		/**
		 * <p>Set delay between steps.</p>
		 * <p><code>
		 * | set step delay to | 500 |
		 * | set step delay to | slow |
		 * | set step delay to | fast |
		 * </code></p>
		 *
		 * @param stepDelay delay in milliseconds
		 */
		public void setStepDelayTo(String stepDelay) {
			if ("slow".equals(stepDelay)) {
				this.stepDelay = 1000;
			} else if ("fast".equals(stepDelay)) {
				this.stepDelay = 0;
			} else {
				this.stepDelay = Long.parseLong(stepDelay);
			}
		}

		/**
		 * <p>In case of an assertion (assert* selenese command), close the browser.</p>
		 * <p><code>
		 * | set stop browser on assertion | true |
		 * </code></p>
		 *
		 * @param stopBrowserOnAssertion
		 */
		public void setStopBrowserOnAssertion(boolean stopBrowserOnAssertion) {
			this.stopBrowserOnAssertion = stopBrowserOnAssertion;
		}

		/**
		 * Instruct the driver to create screenshots
		 * <p><code>
		 * | save screenshot after | <i>failure</i> |
		 * | save screenshot after | <i>error</i> |
		 * </code></p>
		 *
		 * <p><code>
		 * | save screenshot after | <i>every step</i> |
		 * | save screenshot after | <i>step</i> |
		 * </code></p>
		 *
		 * <p><code>
		 * | save screenshot after | <i>nothing</i> |
		 * | save screenshot after | <i>none</i> |
		 * </code></p>
		 */
		public void saveScreenshotAfter(String policy) throws IOException {
			screenCapture.setScreenshotPolicy(policy);
		}

		/**
		 * <p><code>
		 * | save screenshot after | <i>failure</i> | in folder | <i>http://files/testResults/screenshots/${PAGE_NAME} |
		 * | save screenshot after | <i>error</i> |
		 * </code></p>
		 */
		public void saveScreenshotAfterInFolder(String policy, String baseDir) throws IOException {
			screenCapture.setScreenshotBaseDir(removeAnchorTag(baseDir));
			saveScreenshotAfter(policy);
		}

		/**
		 * <p><code>
		 * | ensure | do | <i>open</i> | on | <i>/</i> |
		 * </code></p>
		 *
		 * @param command
		 * @param target
		 * @return
		 */
		public boolean doOn(final String command, final String target) {
			for(int i = 0; i < driverFixtureArray.size(); i++){
				LOG.info("Performing | " + command + " | " + target + " |" + " on thread " + i);
				driverFixtureArray.get(i).doOn(command, target);
			}
			return executeDoCommand(command, new String[] { unalias(target) });
		}

		/**
		 * <p><code>
		 * | ensure | do | <i>type</i> | on | <i>searchString</i> | with | <i>some text</i> |
		 * </code></p>
		 *
		 * @param command
		 * @param target
		 * @param value
		 * @return
		 */
		public boolean doOnWith(final String command, final String target, final String value) {
			for(int i = 0; i < driverFixtureArray.size(); i++){
			LOG.info("Performing | " + command + " | " + target + " | " + value + " |" + " on thread " + i);
				driverFixtureArray.get(i).doOnWith(command, target, value);
			}
			return executeDoCommand(command, new String[] { unalias(target), unalias(value) });
		}

		/**
		 * <p><code>
		 * | <i>$title=</i> | is | <i>getTitle</i> |
		 * </code></p>
		 *
		 * @param command
		 * @return
		 */
		public String is(final String command) {
			LOG.info("Obtain result from  | " + command + " |");
			return is(command, new String[]{});
		}

		/**
		 * Same as {@link #is(String)}, only with "on" statement, analog to "do-on" command.
		 *
		 * @param command
		 * @return
		 */
		public String isOn(final String command) {
			return is(command);
		}

		/**
		 * <p><code>
		 * | <i>$pageName=</i> | is | <i>getText</i> | on | <i>//span</i> |
		 * </code></p>
		 *
		 * @param command
		 * @param target
		 * @return
		 */
		public String isOn(final String command, final String target) {
			LOG.info("Obtain result from | " + command + " | " + target + " |");
			return is(command, new String[] { unalias(target) });
		}

	    public String is(final String command, final String[] parameters) {
	        ExtendedSeleniumCommand seleniumCommand = new ExtendedSeleniumCommand(command);
	        String output = executeCommand(seleniumCommand, parameters, stepDelay);

	        if (seleniumCommand.isBooleanCommand() && seleniumCommand.isNegateCommand()) {
	             if ("true".equals(output)) {
	                output = "false";
	            } else if ("false".equals(output)) {
	                output = "true";
	            } else {
	                throw new IllegalStateException("Illegal boolean value: '" + output + "'");
	            }
	        }

	        return output;
	    }

		/**
		 * Same as {@link #isOn(String, String)}, only with "with" statement, analog to "do-on-with" command.
		 *
		 * @param command
		 * @param target
		 * @return
		 */
		public String isOnWith(final String command, final String target) {
			return isOn(command, target);
		}

		/**
		 * Add a new locator alias to the fixture.
		 *
		 * @param alias
		 * @param locator
		 */
		public void addAliasForLocator(String alias, String locator) {
			LOG.info("Add alias: '" + alias + "' for '" + locator + "'");
			aliases.put(alias, locator);
		}

		/**
		 * Clear the aliases table.
		 */
		public void clearAliases() {
			aliases.clear();
		}

		private String unalias(String value) {
			String result = value;
			if (value != null && value.startsWith(ALIAS_PREFIX)) {
				String alias = value.substring(ALIAS_PREFIX.length());
				String subst = aliases.get(alias);
				if (subst != null) {
				    LOG.info("Expanded alias '" + alias + "' to '" + result + "'");
				    result = subst;
				}
			}
			return result;
		}

		private boolean executeDoCommand(final String methodName, final String[] values) {

			final ExtendedSeleniumCommand command = new ExtendedSeleniumCommand(methodName);

			String output = null;
			boolean result = true;

			if (!locatorCheck.verifyElementPresent(command, values)) {
				result = false;
			} else if (command.requiresPolling()) {
				long timeoutTime = System.currentTimeMillis() + timeout;

				do {
					output = executeCommand(command, values, pollDelay);
					result = checkResult(command, values[values.length - 1], output);
				} while (!result && timeoutTime > System.currentTimeMillis());

				LOG.info("WaitFor- command '" + command.getSeleniumCommand() +  (result ? "' succeeded" : "' failed"));

			} else {

				output = executeCommand(command, values, stepDelay);

				if (command.isCaptureEntirePageScreenshotCommand()) {
					writeToFile(values[0], output);
					result = true;
				} else if (command.isAssertCommand() || command.isVerifyCommand() || command.isWaitForCommand()) {
					String expected = values[values.length - 1];
					result = checkResult(command, expected, output);
					LOG.info("Command '" + command.getSeleniumCommand() + "' returned '" + output + "' => " + (result ? "ok" : "not ok, expected '" + expected + "'"));
				} else {
					LOG.info("Command '" + command.getSeleniumCommand() + "' returned '" + output + "'");
				}
			}

			if (screenCapture.requireScreenshot(command, result)) {
				screenCapture.captureScreenshot(methodName, values);
			}

			if (!result && command.isAssertCommand()) {
				if (stopBrowserOnAssertion) {
					stopBrowser();
				}
				throw new AssertionAndStopTestError(output);
			}

			return result;
		}

		private String executeCommand(final ExtendedSeleniumCommand command, final String[] values, long delay) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("executeCommand. Command: " + command.getSeleniumCommand() + " with values: [" + join(values, ", ") +"]");
			}

			if (commandProcessor == null) {
				throw new IllegalStateException("Command processor not running. " +
								"First start it by invoking startBrowserOnUrl.");
			}

			// Handle special cases first
			if ("pause".equals(command.getSeleniumCommand())) {
				try {
					Thread.sleep(Long.parseLong(values[0]));
				} catch (Exception e) {
					LOG.warn("Pause command interrupted", e);
				}
				return null;
			}

			String output = null;
			try {
				if (command.returnTypeIsArray()) {
					output = executeArrayCommand(command.getSeleniumCommand(), values);
				} else {
					output = executeCommand(command.getSeleniumCommand(), values);
				}
			} catch (final SeleniumException e) {
				output = "Execution of command failed: " + e.getMessage();
				LOG.error(output);
				if (!(command.isAssertCommand() || command.isVerifyCommand() || command.isWaitForCommand())) {
					throw e;
				}
			}

			if (command.isAndWaitCommand()) {
				commandProcessor.doCommand("waitForPageToLoad", new String[] { "" + timeout });
			}

			if (delay > 0) {
				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					LOG.warn("Step delay sleep command interrupted", e);
				}
			}
			return output;
		}

		private String executeCommand(String methodName, final String[] values) {
			String output = commandProcessor.doCommand(methodName, values);
			
			if (output != null && LOG.isDebugEnabled()) {
				LOG.debug("Command processor returned '" + output + "'");
			}
			
			if (output==""){
				return null;
			}
				return output;
			}

		private String executeArrayCommand(String methodName, final String[] values) {
			String[] output = commandProcessor.getStringArray(methodName, values);

			if (output != null && LOG.isDebugEnabled()) {
				LOG.debug("Command processor returned '" + Arrays.asList(output) + "'");
			}

			return stringArrayToString(output);
		}

		private boolean checkResult(ExtendedSeleniumCommand command, String expected, String actual) {
			return command.matches(expected, actual);
		}

		private void writeToFile(final String filename, final String output) {
			File file = asFile(filename);
			try {
	            createParentDirectoryIfNeeded(file);

				ScreenCapture.writeToFile(file, output);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	    private void createParentDirectoryIfNeeded(File file) throws IOException {
	        if (file.getParentFile() != null && !file.getParentFile().exists()) {
	            if (!file.getParentFile().mkdirs()) {
	                throw new IOException("Could not create parent directory " + file.getParent());
	            }
	        }
	    }

	    public void stopBrowser() {
	    	commandProcessor.stop();
	    	commandProcessor=null;
			LOG.info("Command processor stopped");
		}

		public CommandProcessor getCommandProcessor() {
			return commandProcessor;
		}

		public WebDriver getWebDriver() {
			return commandProcessor instanceof WebDriverCommandProcessor
					? ((WebDriverCommandProcessor) commandProcessor).getWrappedDriver()
					: null;
		}
		
		public void registerDriver(WebDriver driver){
			driverArray.add(driver);
			LOG.info("Thread " + driver + " has been registered.");
		}
	}