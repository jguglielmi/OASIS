<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs">
  <xsl:output  method="text" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
  
  <xsl:template match="/">
    
    <xsl:text disable-output-escaping="yes">&lt;html&gt;
    &lt;style&gt;
    tr.scenario > td:first-child {
      background-image: url("../img/minus-sign.png");
      background-position: 2px 7px;
    }
    tr.scenario.closed > td:first-child {
      background-image: url("../img/plus-sign.png");
      padding-left: 18px;
    }
	div.collapsible.closed {
	  display: none;
	}
    .pass, span.pass * {
      background-color: #C1E2B3;
      color: #081109;
    }
	.fail, span.fail * {
      background-color: #FFAAAA;
    }
    .ignore, span.ignore * {
      background-color: #D3D3D3;
    }
    span.error, span.error * {
      background-position: 0 center, -2px center;
      border: 1px solid #985F0D;
      border-radius: 4px;
      padding-right: 3px;
    }
    .ignore, span.ignore * {
      background-color: #AFD9EE;
      color: #0A171D;
    }
	table {
      border: 1px solid #DDDDDD;
      margin-bottom: 20px;
      width: auto;
	  border-collapse: collapse;
      border-spacing: 0;
    }
	table > thead > tr > th, table > tbody > tr > th, table > tfoot > tr > th, table > thead > tr > td, table > tbody > tr > td, table > tfoot > tr > td {
      border: 1px solid #DDDDDD;
    }
	table > thead > tr > th, table > tbody > tr > th, table > tfoot > tr > th, table > thead > tr > td, table > tbody > tr > td, table > tfoot > tr > td {
      padding: 5px;
    }
    &lt;/style&gt;
	&lt;script type="text/javascript"&gt;
	window.onload = function() {
		showImageFilenames();
	}
	function showImageFilenames() {
		var img;
		var allImg=document.getElementsByTagName("img");
		var i = 0;
		while (img = allImg[i++])
		{
			if (img.src.indexOf("?sik&amp;img=") != -1)
			{
				img.parentNode.innerHTML = img.parentNode.innerHTML + ' ' + img.src.substring(img.src.indexOf("?sik&amp;img=") + 9);
			}
		}
	}
	&lt;/script&gt;
    </xsl:text>
      <xsl:text disable-output-escaping="yes">&lt;h2&gt;</xsl:text>
        <xsl:value-of select="//rootPath" disable-output-escaping="yes" />
      <xsl:text disable-output-escaping="yes">&lt;/h2&gt;</xsl:text>
	  <xsl:text disable-output-escaping="yes">&lt;br /&gt;</xsl:text>
	  
	  <xsl:text disable-output-escaping="yes">&lt;b&gt;Counts:&lt;/b&gt;</xsl:text>
	  <xsl:text disable-output-escaping="yes">&lt;p style="margin-left:50px;"&gt;</xsl:text>
	  <xsl:text disable-output-escaping="yes">right: </xsl:text> <xsl:value-of select="sum(//counts/right)" disable-output-escaping="yes" />
	  <xsl:text disable-output-escaping="yes">&lt;br /&gt;wrong: </xsl:text> <xsl:value-of select="sum(//counts/wrong)" disable-output-escaping="yes" />
	  <xsl:text disable-output-escaping="yes">&lt;br /&gt;ignores: </xsl:text> <xsl:value-of select="sum(//counts/ignores)" disable-output-escaping="yes" />
	  <xsl:text disable-output-escaping="yes">&lt;br /&gt;exceptions: </xsl:text> <xsl:value-of select="sum(//counts/exceptions)" disable-output-escaping="yes" />
	  <xsl:text disable-output-escaping="yes">&lt;br /&gt;</xsl:text>
	  <xsl:text disable-output-escaping="yes">&lt;/p&gt;</xsl:text>
	  
      <xsl:for-each select="//content">
        <xsl:value-of select="." disable-output-escaping="yes" />
      </xsl:for-each>
      
    <xsl:text disable-output-escaping="yes">&lt;/html&gt;</xsl:text>
  </xsl:template>  
  
</xsl:stylesheet>
