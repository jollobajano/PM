<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
	xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
	xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" encoding="UTF-8" />

	<xsl:variable name="x">
		ggg
	</xsl:variable>


	<xsl:variable name="headers">
		<header short="audience" long="Gäller för (målgrupp)" />
		<header short="created" long="Ursprungsdatum" />
		<header short="updated" long="Gäller fr o m" />
		<header short="expires" long="Gäller längst t o m" />
		<header short="title" long="Titel" />
		<header short="type" long="Dokumenttyp" />
		<header short="chapter" long="Kapitel" />
		<header short="page" long="Sida" />
		<header short="author" long="Författare/dokumentansvarig" />
		<header short="approver" long="Godkännare" />
	</xsl:variable>

	<xsl:template match="/">
		<documentinfo>
			<xsl:apply-templates
				select="//style:header[1]/table:table/table:table-row[position() > 2]/table:table-cell" />
		</documentinfo>
	</xsl:template>



	<xsl:template match="table:table-cell">
		<item>
			<xsl:apply-templates select="text:p" />
		</item>
	</xsl:template>



	<xsl:template match="text:p[position() = 1]">
		<xsl:attribute name="name"><xsl:value-of select="." /></xsl:attribute>
	</xsl:template>


	<xsl:template match="text:p[position() = 2]">
		<xsl:attribute name="value"><xsl:value-of select="." /></xsl:attribute>
	</xsl:template>


<!-- 	Gamla libreofficeversionen 												-->
<!-- 	<p><span>Text</span><line-break/><span>Text</span><span>Text</span>... 	-->
<!--  																			-->
	<xsl:template match="text:p[child::text:line-break]">
		<xsl:apply-templates select="text:line-break" />
	</xsl:template>


	<xsl:template match="text:line-break">
		<xsl:attribute name="name"><xsl:apply-templates select="preceding-sibling::*|preceding-sibling::text()" /></xsl:attribute>
		<xsl:attribute name="value"><xsl:apply-templates select="following-sibling::*|following-sibling::text()" /></xsl:attribute>
	</xsl:template>


<!-- 	Nya libreofficeversionen 												-->
<!-- 	<p><span>Text<line-break/></span><span>Text</span><span>Text</span>... 	-->
<!--  																			-->
	<xsl:template match="text:p[text:span/text:line-break]">
		<xsl:apply-templates select="text:span[text:line-break]" />		
	</xsl:template>
	

	<xsl:template match="text:span[text:line-break]">
		<xsl:variable name="br" select="position()" />
<!-- 		<xsl:attribute name="name"><xsl:apply-templates select="../text:span[position &lt;= $br]//text()" /></xsl:attribute> -->
		<xsl:attribute name="name"><xsl:apply-templates select="text()" /></xsl:attribute>
		<xsl:attribute name="value"><xsl:apply-templates select="following-sibling::*|following-sibling::text()" /></xsl:attribute>
	</xsl:template>


</xsl:stylesheet>

