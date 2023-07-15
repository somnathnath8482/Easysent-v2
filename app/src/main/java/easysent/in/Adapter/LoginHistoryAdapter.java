package easysent.in.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import easysent.in.Helper.MethodClass;
import easysent.in.R;
import easysent.in.Response.LoginHistory.HistoryItem;
import easysent.in.databinding.LoginHistoryItemBinding;


public class LoginHistoryAdapter extends ListAdapter<HistoryItem,LoginHistoryAdapter.ViewHolder> {

    public LoginHistoryAdapter(@NonNull DiffUtil.ItemCallback<HistoryItem> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HistoryItem item = getItem(position);
        holder.binding.device.setText(item.getManufacturer()+" "+item.getModel());
        holder.binding.date.setText(MethodClass.changeDateFormat(item.getCreatedAt()));
        if (position==0){
            holder.binding.status.setText("This device (Currently active)");
            holder.binding.status.setVisibility(View.VISIBLE);
        }else if (item.getDeviceId().equals(Build.ID)){
            holder.binding.status.setText("This device");
            holder.binding.status.setVisibility(View.VISIBLE);
        }else{
            holder.binding.status.setText("");
            holder.binding.status.setVisibility(View.GONE);
        }

    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{
        LoginHistoryItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding  = LoginHistoryItemBinding.bind(itemView);
        }
    }
}
