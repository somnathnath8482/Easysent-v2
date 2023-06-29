package easysent.in.Room.Block;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;


public class BlockViewModel extends AndroidViewModel {
    private final BlockRepository repository;
    private final LiveData<List<Block>> all_blocks;

    public BlockViewModel(@NonNull Application application) {
        super(application);
        repository = new BlockRepository(application);
        all_blocks = repository.getAll();
    }

    public void insert(Block user) {
        repository.insert(user);
    }

 
    public void delete(String user) {
        repository.delete(user);
    }
public void deletefromto(String from,String to) {
        repository.deleteFromTo(from,to);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public  LiveData<Block>select_blok(String me,String reciver) {

        return repository.selectBlock(me, reciver);
    }

    public LiveData<List<Block>> search() {
        return repository.getAll();
    }

   



}
