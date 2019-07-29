package br.com.ronanlima.twolevelmenu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import br.com.ronanlima.twolevelmenu.adapter.ItemMenuAdapter;
import br.com.ronanlima.twolevelmenu.adapter.MenuAdapter;
import br.com.ronanlima.twolevelmenu.database.AppDatabase;
import br.com.ronanlima.twolevelmenu.interfaces.MenuItemClickListener;
import br.com.ronanlima.twolevelmenu.model.Menu;
import br.com.ronanlima.twolevelmenu.util.MenuUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MenuItemClickListener {

    public static final String ID_USUARIO = "idUsuario";
    public static final int LIMIT_MORE_USED_MENUS = 3; //altere conforme sua necessidade

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_menu_selected)
    LinearLayout llMenuSelected;
    EditText search;
    NavigationView navigationView;
    ImageButton btReset;
    ImageView fotoUsuarioImageView;
    TextView descricaoUsuarioTextView;
    TextView apelidoTextView;
    View layoutEditarConta;
    ProgressBar loadingFoto;
    private MenuAdapter menuAdapter;
    private ItemMenuAdapter itemMenuAdapter;
    private List<Menu> menus;
    private Menu menuSelecionado;
    private boolean isPrimeiroNivelMenuSelecionado = true;
    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(this);

        setSupportActionBar(toolbar);
        menuAdapter = new MenuAdapter(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bindLayoutNavHeader();
        loadMenu();
        configRecyclerView();
        llMenuSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Faz o bind do layout que está dentro do nav_header, da NavigationView
     */
    private void bindLayoutNavHeader() {
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        btReset = headerView.findViewById(R.id.bt_reset);
        search = headerView.findViewById(R.id.search);
        fotoUsuarioImageView = headerView.findViewById(R.id.fotoUsuarioImageView);
        descricaoUsuarioTextView = headerView.findViewById(R.id.descUsuarioTextview);
        apelidoTextView = headerView.findViewById(R.id.nomeUsuarioTextview);
        layoutEditarConta = headerView.findViewById(R.id.layout_editar_perfil);
        loadingFoto = headerView.findViewById(R.id.loading_foto);
    }

    private void loadMenu() {
        try {
            String str = MenuUtil.openRawResource(this, R.raw.menu_pessoal_json);
            menus = MenuUtil.fromJsonArrayToList(str);
            menuAdapter.setMenus(menus);
            itemMenuAdapter = new ItemMenuAdapter(this, menus);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao carregar o menu");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao carregar o menu");
        }
    }

    private void configRecyclerView() {
        mRecyclerView.setAdapter(menuAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setClipToPadding(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down));
    }

    @Override
    public void onMenuClick(Menu menu) {
        menuSelecionado = menu;
        resetItemChecked(menu.getItems());
        changeMargintTopMenuRecyclerView(R.dimen.margin_top_tv_into_nav_header, menu.getTitulo(), View.VISIBLE);
        isPrimeiroNivelMenuSelecionado = false;
        itemMenuAdapter.setMenu(menu.getItems());
        mRecyclerView.setAdapter(itemMenuAdapter);
        runLayoutAnimation(R.anim.layout_animation_from_right);
    }

    @Override
    public void onItemMenuClick(Menu menu) {
        hideKeyboard();
        if (menu.getLink() != null
                && !"".equalsIgnoreCase(menu.getLink())) {
            Intent i = new Intent(this, SecondActivity.class);
            Bundle b = new Bundle();
            b.putString("item_selected", menu.getTitulo());
            i.putExtras(b);
            startActivity(i);
            drawer.closeDrawer(GravityCompat.START);
            updateClickMenu(menu);
        }
    }

    /**
     * Para cada vez que clicar num MENU_FILHO, grava/altera o registro no banco de dados com a quantidade
     * de clicks do mesmo.
     *
     * @param menu
     */
    private void updateClickMenu(final Menu menu) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                String userId = ID_USUARIO;//recupere o id do seu usuário logado
                menu.setUserId(userId);
                menu.setDataVisualizacao(Calendar.getInstance().getTime());
                int qtdClickFromMenu = appDatabase.menuDAO().getQtdClickFromMenu(userId, menu.getIdMenu());
                menu.setQtdClick(++qtdClickFromMenu);
                int update = appDatabase.menuDAO().update(menu);
                if (update == 0) { // registro não existe
                    appDatabase.menuDAO().insert(menu);
                }
            }
        });
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Muda o marginTop do recyclerView, de acordo com o nível (1 ou 2) do menu. Tratamento criado para
     * exibir o nome do menu pai, com um ícone de 'voltar' ao lado, para facilitar a ação de voltar para o menu anterior.
     *
     * @param resourceDimension
     * @param titulo
     * @param visibility
     */
    private void changeMargintTopMenuRecyclerView(int resourceDimension, String titulo, int visibility) {
        FrameLayout.LayoutParams recyclerViewParams = (FrameLayout.LayoutParams) mRecyclerView.getLayoutParams();
        float marginTopFromMenuSelected = getResources().getDimension(resourceDimension);
        recyclerViewParams.setMargins(0, (int) marginTopFromMenuSelected, 0, 0);
        mRecyclerView.setLayoutParams(recyclerViewParams);
        TextView tvTitleMenu = llMenuSelected.findViewById(R.id.tv_title_menu_selected);
        tvTitleMenu.setText(titulo);
        llMenuSelected.setVisibility(visibility);
    }

    private void resetItemChecked(List<Menu> list) {
        for (Menu m : list) {
            m.setChecked(false);
        }
    }

    private void runLayoutAnimation(int layoutAnimation) {
        Context context = mRecyclerView.getContext();
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, layoutAnimation);
        mRecyclerView.setLayoutAnimation(animationController);
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            isPrimeiroNivelMenuSelecionado = true;
            if (mRecyclerView.getAdapter() instanceof ItemMenuAdapter) {
                menuSelecionado = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeMargintTopMenuRecyclerView(R.dimen.margin_top_nav_header, "", View.GONE);
                        runLayoutAnimation(R.anim.layout_animation_from_left);
                        mRecyclerView.setAdapter(menuAdapter);
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                });
            }
        } else {
            super.onBackPressed();
        }
    }

}
