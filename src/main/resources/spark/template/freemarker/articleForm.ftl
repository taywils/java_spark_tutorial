<div class="starter-template">
    <form class="form-horizontal" role="form" id='article-create-form' method='POST' action='/article/create'>
        <div class="form-group">
            <label class="col-sm-3 control-label" for="title">Title: </label>
            <div class="col-sm-5">
                <input class="form-control" type='text' id="title" name='article-title' placeholder="Enter a new title" />
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label" for="summary">Summary: </label>
            <div class="col-sm-5">
                <input class="form-control" type='text' id="summary" name='article-summary' placeholder="Enter a new summary" />
            </div>
        </div>
    </form>

    <label for="content">Content</label>
    <textarea class="form-control" name='article-content' id="content" rows='4' cols='50' form='article-create-form' placeholder="Enter article content"></textarea>
    <input type='submit' value='Publish' class="btn btn-primary" form='article-create-form' />
</div>