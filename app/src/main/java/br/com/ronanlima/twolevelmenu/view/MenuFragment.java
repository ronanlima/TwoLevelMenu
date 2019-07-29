package br.com.ronanlima.twolevelmenu.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.ronanlima.twolevelmenu.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment {

    @BindView(R.id.tv_item_selected)
    TextView tvMenuName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String itemSelected = getArguments().getString("item_selected");
            tvMenuName.setText(itemSelected);
        }
    }
}
