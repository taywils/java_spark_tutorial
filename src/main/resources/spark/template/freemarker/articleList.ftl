<#if hasNoArticles??>
    <div class="starter-template">
        <h1>${hasNoArticles}</h1>
    </div>
<#else>
    <div class="starter-template">
        <#list articles as article>
            <h3>${article.getTitle()}</h3>
            <br />
            ${article.getCreatedAt()}
            <br />
            ${article.getSummaryLink()}
            <br />
            ${article.getEditLink()} | ${article.getDeleteLink()}
        </#list>
    </div>
</#if>