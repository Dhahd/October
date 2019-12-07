package kanielOutis.october.recyclerView;

import android.view.ViewGroup;
import kanielOutis.october.data.BaseViewHolder;

public interface ViewHolderHelper<V> {
    BaseViewHolder<V> bindView(ViewGroup parent);
}