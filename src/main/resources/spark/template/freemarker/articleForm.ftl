<div class="starter-template">
    <form class="form-horizontal" role="form" id='article-create-form' method='POST' <#if article??>action="/article/update/:id"<#else>action="/article/create"</#if>>
        <div class="form-group">
            <label class="col-sm-3 control-label" for="title">Title: </label>
            <div class="col-sm-5">
                <input class="form-control" type='text' id="title" name='article-title' placeholder="Enter a new title" <#if article??>value="${article.getTitle()}"</#if> />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label" for="summary">Summary: </label>
            <div class="col-sm-5">
                <input class="form-control" type='text' id="summary" name='article-summary' placeholder="Enter a new summary" <#if article??>value="${article.getSummary()}"</#if> />
            </div>
        </div>
        <#if article??>
            <input type='hidden' name='article-id' value="${article.getId()}" />
        </#if>
    </form>

    <label for="content">Content</label>
    <textarea class="form-control" name='article-content' id="content" rows='4' cols='50' form='article-create-form' placeholder="Enter article content"><#if article??>${article.getContent()}</#if></textarea>
    <input type='submit' <#if article??>value='Update'<#else>value='Publish'</#if> class="btn btn-primary" form='article-create-form' />
</div>