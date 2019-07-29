package br.com.ronanlima.twolevelmenu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import br.com.ronanlima.twolevelmenu.R;
import br.com.ronanlima.twolevelmenu.interfaces.MenuItemClickListener;
import br.com.ronanlima.twolevelmenu.model.Menu;
import br.com.ronanlima.twolevelmenu.util.MenuUtil;

public class ItemMenuAdapter extends RecyclerView.Adapter<ItemMenuAdapter.ItemMenuVH> {
    private Context mContext;
    private List<Menu> menus;
    private List<Menu> queryMenus;
    private MenuItemClickListener itemMenuClickListener;

    public ItemMenuAdapter(MenuItemClickListener itemMenuClickListener, List<Menu> items) {
        this.menus = new ArrayList<>();
        this.queryMenus = new ArrayList<>();
        this.menus.addAll(items);//sempre possuira todos os menus, para fins de consulta (1o ou 2o nivel)
        this.queryMenus.addAll(items);
        this.itemMenuClickListener = itemMenuClickListener;
    }

    @NonNull
    @Override
    public ItemMenuVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_adapter, parent, false);
        return new ItemMenuVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemMenuVH holder, int position) {
        final Menu menu = queryMenus.get(position);
        holder.getTitulo().setText(menu.getTitulo());
        if (menu.getLink() == null || menu.getLink().trim().isEmpty()) {
            setStyleTitulo(holder, 14f, View.GONE, mContext.getResources().getColor(R.color.colorAccent));
        } else {
            setStyleTitulo(holder, 16f, View.VISIBLE, mContext.getResources().getColor(R.color.texto));
        }
        toggleIconeNovo(holder, menu);
        holder.itemView.setBackgroundColor(mContext.getResources().getColor(menu.getColorBackground()));
    }

    private void setStyleTitulo(ItemMenuVH holder, float textSize, int visibility, int textColor) {
        holder.getViewDivider().setVisibility(visibility);
        holder.getTitulo().setTextSize(textSize);
        holder.getTitulo().setTextColor(textColor);
    }

    private void toggleIconeNovo(@NonNull ItemMenuVH holder, Menu menu) {
        if (menu.isShowBadgeUpdate(menu)) {
            holder.getTvNovo().setVisibility(View.VISIBLE);
        } else {
            holder.getTvNovo().setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return queryMenus == null ? 0 : queryMenus.size();
    }

    public void setMenu(List<Menu> itemMenu) {
        queryMenus.clear();
        queryMenus.addAll(itemMenu);
        notifyDataSetChanged();
    }

    public void updateMenuFromServer(List<Menu> itemMenu) {
        menus.clear();
        menus.addAll(itemMenu);
        setMenu(itemMenu);
    }

    public void filterData(String query, Menu menuSelecionado) {
        query = query.toLowerCase();
        queryMenus.clear();

        if ("".equalsIgnoreCase(query)) {
            queryMenus.addAll(menuSelecionado.getItems());
        } else {

            for (Menu menu : menus) {
                List<Menu> subMenus = menu.getItems();
                List<Menu> newList = new ArrayList<>();
                if (subMenus != null && subMenus.size() > 0) {
                    for (Menu child : subMenus) {
                        compareTitleWithQuery(query, newList, child);
                    }
                } else {
                    if (Normalizer.normalize(menu.getTitulo().toLowerCase(), Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                            .toLowerCase().contains(query.toLowerCase()) && menu.getLink() != null && !"".equalsIgnoreCase(menu.getLink())) {
                        newList.add(menu);
                    }
                }
                addParentMenu(newList, menu.getTitulo());
                queryMenus.addAll(newList);
            }
        }
        notifyDataSetChanged();
    }

    private void addParentMenu(List<Menu> newList, String titulo) {
        if (newList.size() > 0) {
            Menu nContinent = new Menu(null, titulo, newList, Menu.TipoMenu.MENU_FILHO);
            queryMenus.add(nContinent);
        }
    }

    private void compareTitleWithQuery(String query, List<Menu> newList, Menu child) {
        if (Normalizer.normalize(child.getTitulo().toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase().contains(query.toLowerCase()) && child.getLink() != null && !"".equalsIgnoreCase(child.getLink())) {
            newList.add(child);
        }
    }

    class ItemMenuVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titulo;
        private TextView tvNovo;
        private View viewDivider;

        public ItemMenuVH(@NonNull View itemView) {
            super(itemView);
            setTitulo((TextView) itemView.findViewById(R.id.tv_titulo));
            setTvNovo((TextView) itemView.findViewById(R.id.tv_novo));
            setViewDivider(itemView.findViewById(R.id.view_divider));
            itemView.setOnClickListener(this);
        }

        public TextView getTitulo() {
            return titulo;
        }

        public void setTitulo(TextView titulo) {
            this.titulo = titulo;
        }

        public TextView getTvNovo() {
            return tvNovo;
        }

        public void setTvNovo(TextView tvNovo) {
            this.tvNovo = tvNovo;
        }

        public View getViewDivider() {
            return viewDivider;
        }

        public void setViewDivider(View viewDivider) {
            this.viewDivider = viewDivider;
        }

        @Override
        public void onClick(View v) {
            Menu menu;
            if (!queryMenus.isEmpty()) {
                menu = queryMenus.get(getAdapterPosition());
            } else {
                menu = menus.get(getAdapterPosition());
            }

            menu.getTipoMenu().click(itemMenuClickListener, menu);
            if (!menu.getTipoMenu().equals(Menu.TipoMenu.MENU_MORTO)) {
                MenuUtil.checkMenu(menu, queryMenus, ItemMenuAdapter.this, getAdapterPosition());
            }
        }

    }
}
