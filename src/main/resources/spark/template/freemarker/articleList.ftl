<#if hasNoArticles??>
    <div class="starter-template">
        <h1>${hasNoArticles}</h1>
    </div>
<#else>
    <#list articles as article>
        Title: ${article.getTitle()}
        <br />
        ${article.getCreatedAt()}
        <br />
        Summary: ${article.getSummaryLink()}
        <br />
        ${article.getEditLink()} | ${article.getDeleteLink()}
        <p></p>
    </#list>
</#if>