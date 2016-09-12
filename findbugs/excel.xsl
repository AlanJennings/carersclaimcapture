<?xml version="1.0" encoding="UTF-8"?>
<!--
  FindBugs - Find bugs in Java programs
  Copyright (C) 2004,2005 University of Maryland
  Copyright (C) 2005, Chris Nappin
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->


<xsl:stylesheet version="1.0"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output
	method="xml"
	omit-xml-declaration="yes"
	standalone="yes"
         doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
         doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	indent="yes"
	encoding="UTF-8"/>

<xsl:variable name="bugTableHeader">
	<tr class="tableheader">
        <th align="left">Designation</th>
        <th align="left">Designation Notes</th>
        <th align="left">Priority</th>
        <th align="left">Rank</th>
        <th align="left">Long Message</th>
        <th align="left">Class Name</th>
        <th align="left">Lines</th>
        <th align="left">Full Message</th>
        
        <th align="left">Full Class Name</th>
        <th align="left">Category</th>
        <th align="left">Type</th>
        <th align="left">Short Message</th>
        
     </tr>
</xsl:variable>

<xsl:template match="/">
	<html>
	<head>
		<title>FindBugs Report</title>
		<style type="text/css">
		.tablerow0 {
			background: #EEEEEE;
		}

		.tablerow1 {
			background: white;
		}

		.detailrow0 {
			background: #EEEEEE;
		}

		.detailrow1 {
			background: white;
		}

		.tableheader {
			background: #b9b9fe;
			font-size: larger;
		}
        
        .title {
            font-size: 24pt;
        }
        
        .subtitle {
            font-size: 18pt;
        }
        
		</style>
	</head>

	<body> 
	<span class="title"><xsl:value-of select="/BugCollection/Project/@projectName" /> FindBugs Report </span><span class="subtitle">(v<xsl:value-of select="/BugCollection/@version"/>)</span>
    <table>
        <tr>
            <td class="subtitle">Analyzed:</td>
            <td class="subtitle">(USE =TEXT(DATEVALUE("1/1/1970") + (<xsl:value-of  select="BugCollection/@analysisTimestamp"/>/(1000*60*60*24)), "DD/MM/YYYY hh:mm:ss") to convert to a readable timestamp)</td>
        </tr>
    </table>
    <p></p>
    
	<xsl:call-template name="generateWarningTable">
		<xsl:with-param name="warningSet" select="/BugCollection/BugInstance"/>
	</xsl:call-template>

    <p><br/><br/></p>
	<h1><a name="Details">Warning Types</a></h1>

	<xsl:apply-templates select="/BugCollection/BugPattern">
		<xsl:sort select="@abbrev"/>
		<xsl:sort select="ShortDescription"/>
	</xsl:apply-templates>

	</body>
	</html>
</xsl:template>

<xsl:template match="BugInstance">
    <xsl:variable name="warningId">
        <xsl:value-of select="generate-id()" />
    </xsl:variable>

    <tr class="tablerow{position() mod 2}">
        <td width="10%" valign="top"><xsl:value-of select="UserAnnotation/@designation" /></td>
        <td width="40%" valign="top"><xsl:value-of select="UserAnnotation/text()" /></td>
        <td width="10%" valign="top"><xsl:value-of select="@priority" /></td>
        <td width="10%" valign="top"><xsl:value-of select="@rank" /></td>   <!-- Lower is scarier -->
        <td width="70%"> <xsl:value-of select="LongMessage" /> </td>
        <xsl:choose>
            <xsl:when test="SourceLine">
                <td><xsl:value-of select="SourceLine/@sourcefile" /></td>
                <td>
                    <xsl:choose>
                        <xsl:when test="SourceLine/@start = SourceLine/@end">
                            <xsl:value-of select="SourceLine/@start" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="SourceLine/@start" /> to <xsl:value-of select="SourceLine/@end" />
                        </xsl:otherwise>
                    </xsl:choose>
                </td>
                <td>
                    <xsl:for-each select="./*/Message">
                        <xsl:value-of select="text()" />, 
                    </xsl:for-each>
                </td>
                <td><xsl:value-of select="Class/@classname" /></td>
            </xsl:when>
            <xsl:otherwise>
                <td></td>
                <td></td>
                <td></td>
            </xsl:otherwise>
        </xsl:choose>
        <td width="10%" valign="top"><xsl:value-of select="@category" /></td>
        <td width="10%" valign="top"><xsl:value-of select="@type" /></td>
        <td width="20%" valign="top"> <a href="#{@type}"><xsl:value-of select="ShortMessage" /></a></td>        
    </tr>
</xsl:template>

<xsl:template match="BugPattern">
	<h2><a name="{@type}"><xsl:value-of select="ShortDescription"/></a></h2>
	<xsl:value-of select="Details" disable-output-escaping="yes"/>
	<p><br/><br/></p>
</xsl:template>

<xsl:template name="generateWarningTable">
	<xsl:param name="warningSet"/>

	<table class="warningtable" width="100%" cellspacing="2" cellpadding="5">
		<xsl:copy-of select="$bugTableHeader"/>
		<xsl:choose>
		    <xsl:when test="count($warningSet) &gt; 0">
				<xsl:apply-templates select="$warningSet">
					<xsl:sort select="@priority"/>
					<xsl:sort select="@abbrev"/>
					<xsl:sort select="Class/@classname"/>
				</xsl:apply-templates>
		    </xsl:when>
		    <xsl:otherwise>
		        <tr><td colspan="2"><p><i>None</i></p></td></tr>
		    </xsl:otherwise>
		</xsl:choose>
	</table>
	<p><br/><br/></p>
</xsl:template>

</xsl:stylesheet>
