<%@ tag description="Hint Tag" pageEncoding="UTF-8"%>

<%@ attribute name="trackingId" required="true" %>

<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
    
    ga('create', '${pageScope.trackingId}', {
      'siteSpeedSampleRate': '1'
    });
    ga('send', 'pageview');
    ga('set', 'anonymizeIp', true);
    ga('set', 'nonInteraction', true);
</script>