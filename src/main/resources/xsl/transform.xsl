<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="book">
		<html>
			<body>
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
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet> 