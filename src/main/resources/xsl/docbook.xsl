<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="book">
		<h1>
			<xsl:value-of select="title" />
		</h1>
		<xsl:for-each select="chapter">
			<div>
				<h3>
					<xsl:value-of select="title" />
				</h3>
				<xsl:for-each select="para">
					<xsl:apply-templates />
				</xsl:for-each>
			</div>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet> 