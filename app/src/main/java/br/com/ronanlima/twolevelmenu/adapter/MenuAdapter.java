package br.com.ronanlima.twolevelmenu.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.ronanlima.twolevelmenu.R;
import br.com.ronanlima.twolevelmenu.interfaces.MenuItemClickListener;
import br.com.ronanlima.twolevelmenu.model.Menu;
import br.com.ronanlima.twolevelmenu.util.MenuUtil;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuVH> {
    public static final String TAG = MenuAdapter.class.getCanonicalName().toUpperCase();
    private Context mContext;
    private List<Menu> menus;
    private MenuItemClickListener menuItemClickListener;

    public MenuAdapter(MenuItemClickListener menuItemClickListener) {
        this.menuItemClickListener = menuItemClickListener;
        this.menus = new ArrayList<>();
    }

    @NonNull
    @Override
    public MenuVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.menu_adapter, parent, false);
        return new MenuVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuVH holder, int position) {
        final Menu menu = menus.get(position);
        toggleIcone(holder, menu);
        holder.getTitulo().setText(menu.getTitulo());
        holder.itemView.setBackgroundColor(mContext.getResources().getColor(menu.getColorBackground()));
    }

    private void toggleIcone(@NonNull final MenuVH holder, final Menu menu) {
        if (menu.getIcone() == null || menu.getIcone().isEmpty()) {
            changeVisibility(menu, holder, View.INVISIBLE);
        } else {
            changeVisibility(menu, holder, View.VISIBLE);
            setImageDrawable(holder, menu);
        }
        toggleIconeNovo(holder, menu);
    }

    private void setImageDrawable(@NonNull MenuVH holder, Menu menu) {
        int resourceID = mContext.getResources().getIdentifier(menu.getIcone(), "drawable", mContext.getPackageName());
        try {
            Drawable drawable = AppCompatResources.getDrawable(mContext, resourceID);
            if (drawable != null) {
                holder.getIcone().setImageDrawable(drawable);
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, e.getMessage());
            holder.getIcone().setImageDrawable(AppCompatResources.getDrawable(mContext, R.drawable.icone_mais_acessados));// Apenas para fins de layout, estou setando o icone de estrela
        }
    }

    private void changeVisibility(Menu menu, MenuVH holder, int visibility) {
        if (menu.getIdMenu() == Menu.ID_MAIS_UTILIZADOS) {
            holder.getIconeDetalhe().setVisibility(View.GONE);
        } else {
            holder.getIconeDetalhe().setVisibility(visibility);
        }
        holder.getIcone().setVisibility(visibility);
    }

    private void toggleIconeNovo(@NonNull MenuVH holder, Menu menu) {
        if (menu.isParentShowBadgeUpdate()) {
            holder.getIvNovo().setVisibility(View.VISIBLE);
        } else {
            holder.getIvNovo().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return menus != null ? menus.size() : 0;
    }

    public void setMenus(List<Menu> list) {
        menus.clear();
        menus.addAll(list);
        notifyDataSetChanged();
    }

    public void clearMoreUsed(List<Integer> index) {
        for (Integer i : index) {
            menus.remove(i.intValue());
        }
        for (Menu m : menus) {
            for (Menu m2 : m.getItems()) {
                m2.setDataVisualizacao(null);
            }
        }
        notifyDataSetChanged();
    }

    class MenuVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icone;
        private TextView titulo;
        private ImageView ivNovo;
        private ImageView iconeDetalhe;
        private View viewDivider;

        public MenuVH(@NonNull View itemView) {
            super(itemView);
            setIcone((ImageView) itemView.findViewById(R.id.iv_menu));
            setTitulo((TextView) itemView.findViewById(R.id.tv_titulo));
            setIvNovo((ImageView) itemView.findViewById(R.id.iv_novo));
            setIconeDetalhe((ImageView) itemView.findViewById(R.id.iv_detalhe));
            setViewDivider(itemView.findViewById(R.id.view_divider));
            itemView.setOnClickListener(this);
        }

        public ImageView getIcone() {
            return icone;
        }

        public void setIcone(ImageView icone) {
            this.icone = icone;
        }

        public TextView getTitulo() {
            return titulo;
        }

        public void setTitulo(TextView titulo) {
            this.titulo = titulo;
        }

        public ImageView getIvNovo() {
            return ivNovo;
        }

        public void setIvNovo(ImageView ivNovo) {
            this.ivNovo = ivNovo;
        }

        public ImageView getIconeDetalhe() {
            return iconeDetalhe;
        }

        public void setIconeDetalhe(ImageView iconeDetalhe) {
            this.iconeDetalhe = iconeDetalhe;
        }

        public View getViewDivider() {
            return viewDivider;
        }

        public void setViewDivider(View viewDivider) {
            this.viewDivider = viewDivider;
        }

        @Override
        public void onClick(View v) {
            Menu menu = menus.get(getAdapterPosition());
            menu.getTipoMenu().click(menuItemClickListener, menu);
            if (!menu.getTipoMenu().equals(Menu.TipoMenu.MENU_MORTO)) {
                MenuUtil.checkMenu(menu, menus, MenuAdapter.this, getAdapterPosition());
            }
        }

    }

}
