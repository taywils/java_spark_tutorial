import java.util.ArrayList;

public class ArticleServletDao<T extends Article> implements ArticleDbService<T> {
    ArrayList<T> storage;

    public ArticleServletDao() {
        storage = new ArrayList<T>();
    }

    @Override
    public Boolean create(T entity) {
        storage.add(entity);
        return null;
    }

    @Override
    public T readOne(int id) {
        return storage.get(id);
    }

    @Override
    public ArrayList<T> readAll() {
        return storage;
    }

    @Override
    public Boolean update(int id, String title, String summary, String content) {
        T entity = storage.get(id);

        entity.setSummary(summary);
        entity.setTitle(title);
        entity.setContent(content);

        return true;
    }

    @Override
    public Boolean delete(int id) {
        storage.get(id).delete();
        return true;
    }
}
