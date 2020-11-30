package ch.bolkhuis.kasboek.components;

import ch.bolkhuis.kasboek.core.AccountType;
import ch.bolkhuis.kasboek.core.AccountingEntity;
import ch.bolkhuis.kasboek.core.InmateEntity;
import ch.bolkhuis.kasboek.core.PlaceholderEntity;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Vector;

/**
 * AccountingEntityTreeTableView is an implementation of a {@code TreeTableView<AccountingEntity>}. The class listens for
 * changes in its own backing ObservableMap and updates its leaves accordingly.
 *
 * @author Aron Hoogeveen
 */
public class AccountingEntityTreeTableView extends TreeTableView<AccountingEntity> implements MapChangeListener<Integer, AccountingEntity> {
    // Backing ObservableMap
    private final ObservableMap<Integer, AccountingEntity> items;
    // Root TreeItems
    private final TreeItem<AccountingEntity> inmatesRoot = new TreeTableRootItem<>(new PlaceholderEntity("Huischgenoten"));
    private final TreeItem<AccountingEntity> assetsRoot = new TreeTableRootItem<>(new PlaceholderEntity("Assets"));
    private final TreeItem<AccountingEntity> expensesRoot = new TreeTableRootItem<>(new PlaceholderEntity("Expenses"));
    private final TreeItem<AccountingEntity> liabilitiesRoot = new TreeTableRootItem<>(new PlaceholderEntity("Liabilities"));
    private final TreeItem<AccountingEntity> dividendsRoot = new TreeTableRootItem<>(new PlaceholderEntity("Dividends"));
    private final TreeItem<AccountingEntity> revenuesRoot = new TreeTableRootItem<>(new PlaceholderEntity("Revenues"));
    private final TreeItem<AccountingEntity> equitiesRoot = new TreeTableRootItem<>(new PlaceholderEntity("Equities"));

    /**
     * Creates a TreeTableView populated with the AccountingEntities from the ObservableMap {@code items}.
     *
     * @param items the AccountingEntities to put in this TreeTableView
     */
    public AccountingEntityTreeTableView(@NotNull ObservableMap<Integer, AccountingEntity> items) {
        if (items == null) { throw new NullPointerException(); }
        this.items = items;

        setShowRoot(false);
        items.addListener(this); // listen for changes in the backing ObservableMap

        setCustomColumns();
        initChildren();
    }

    /**
     * Sets the Column Factories for this TreeTableView.
     */
    private void setCustomColumns() {
        TreeTableColumn<AccountingEntity, String> nameColumn = new TreeTableColumn<>("Naam");
        nameColumn.setCellFactory(param -> new AccountingEntityTreeTableCell());
        TreeTableColumn<AccountingEntity, String> balanceColumn = new TreeTableColumn<>("Balans");

        nameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AccountingEntity, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName())
        );
        // Only set the balance for TreeItems which are not roots
        balanceColumn.setCellValueFactory(param -> {
            if (!param.getValue().isLeaf()) {
                return null;
            }
            return new ReadOnlyStringWrapper(NumberFormat.getCurrencyInstance().format(param.getValue().getValue().getBalance()));
        });

        // Set preferred widths for the columns
        nameColumn.setPrefWidth(300);
        balanceColumn.setPrefWidth(100);

        getColumns().addAll(
                nameColumn,
                balanceColumn
        );
    }

    /**
     * Populate all the root items with leaves.
     */
    private void initChildren() {
        TreeItem<AccountingEntity> root = new TreeTableRootItem<>(new PlaceholderEntity("Entiteiten"));

        Vector<TreeItem<AccountingEntity>> inmateLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> assetLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> expenseLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> liabilityLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> dividendLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> revenueLeaves = new Vector<>();
        Vector<TreeItem<AccountingEntity>> equityLeaves = new Vector<>();

        // populate the vectors with leaves
        for (AccountingEntity item : items.values()) {
            if (item instanceof InmateEntity) {
                inmateLeaves.add(new TreeItem<>(item));
            } else {
                TreeItem<AccountingEntity> leaf = new TreeItem<>(item);
                if (item.getAccountType().equals(AccountType.ASSET)) {
                    assetLeaves.add(leaf);
                } else if (item.getAccountType().equals(AccountType.EXPENSE)) {
                    expenseLeaves.add(leaf);
                } else if (item.getAccountType().equals(AccountType.LIABILITY)) {
                    liabilityLeaves.add(leaf);
                } else if (item.getAccountType().equals(AccountType.DIVIDEND)) {
                    dividendLeaves.add(leaf);
                } else if (item.getAccountType().equals(AccountType.REVENUE)) {
                    revenueLeaves.add(leaf);
                } else if (item.getAccountType().equals(AccountType.EQUITY)) {
                    equityLeaves.add(leaf);
                } else {
                    System.err.println("AccountType \"" + item.getAccountType() + "\" not supported by class " +
                            "AccountingEntityTreeTableView for item with id: " + item.getId() + ".");
                }
            }
        }

        // Clear the children of the root and then add the new leaves. This makes this method suitable for resetting
        // this TreeTableView with a different backing ObservableMap.
        inmatesRoot.getChildren().clear();
        inmatesRoot.getChildren().addAll(inmateLeaves);
        assetsRoot.getChildren().clear();
        assetsRoot.getChildren().addAll(assetLeaves);
        expensesRoot.getChildren().clear();
        expensesRoot.getChildren().addAll(expenseLeaves);
        liabilitiesRoot.getChildren().clear();
        liabilitiesRoot.getChildren().addAll(liabilityLeaves);
        dividendsRoot.getChildren().clear();
        dividendsRoot.getChildren().addAll(dividendLeaves);
        revenuesRoot.getChildren().clear();
        revenuesRoot.getChildren().addAll(revenueLeaves);
        equitiesRoot.getChildren().clear();
        equitiesRoot.getChildren().addAll(equityLeaves);

        root.getChildren().addAll(
                inmatesRoot,
                assetsRoot,
                expensesRoot,
                liabilitiesRoot,
                dividendsRoot,
                revenuesRoot,
                equitiesRoot
        );

        // expand all roots
        inmatesRoot.setExpanded(true);
        assetsRoot.setExpanded(true);
        expensesRoot.setExpanded(true);
        liabilitiesRoot.setExpanded(true);
        dividendsRoot.setExpanded(true);
        revenuesRoot.setExpanded(true);
        equitiesRoot.setExpanded(true);

        setRoot(root);
    }


    /**
     * Called after a change has been made to an ObservableMap.
     * This method is called on every elementary change (put/remove) once.
     * This means, complex changes like keySet().removeAll(Collection) or clear()
     * may result in more than one call of onChanged method.
     *
     * @param change the change that was made
     */
    @Override
    public void onChanged(Change<? extends Integer, ? extends AccountingEntity> change) {
        // Get the AccountingEntity corresponding to this change
        AccountingEntity entity = change.wasAdded() ? change.getValueAdded() : change.getValueRemoved();
        if (entity == null) {
            throw new IllegalArgumentException("All values of the TreeItems in the TreeTableView must not be null");
        }
        TreeItem<AccountingEntity> leaf = new TreeItem<>(entity);

        if (change.wasAdded()) {
            if (entity instanceof InmateEntity) {
                inmatesRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.ASSET)) {
                assetsRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.EXPENSE)) {
                expensesRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.LIABILITY)) {
                liabilitiesRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.DIVIDEND)) {
                dividendsRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.REVENUE)) {
                revenuesRoot.getChildren().add(leaf);
            } else if (entity.getAccountType().equals(AccountType.EQUITY)) {
                equitiesRoot.getChildren().add(leaf);
            } else {
                System.err.println("Added change event: AccountType \"" + entity.getAccountType() + "\" not supported by class " +
                        "AccountingEntityTreeTableView for item with id: " + entity.getId() + ".");
            }
        }
        if (change.wasRemoved()) {
            if (entity instanceof InmateEntity) {
                inmatesRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.ASSET)) {
                assetsRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.EXPENSE)) {
                expensesRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.LIABILITY)) {
                liabilitiesRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.DIVIDEND)) {
                dividendsRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.REVENUE)) {
                revenuesRoot.getChildren().remove(leaf);
            } else if (entity.getAccountType().equals(AccountType.EQUITY)) {
                equitiesRoot.getChildren().remove(leaf);
            } else {
                System.err.println("Removed change event: AccountType \"" + entity.getAccountType() + "\" not supported by class " +
                        "AccountingEntityTreeTableView for item with id: " + entity.getId() + ".");
            }
        }
    }

    /**
     * A TreeTableCell for AccountingEntities.
     */
    class AccountingEntityTreeTableCell extends TextFieldTreeTableCell<AccountingEntity, String> {
        /**
         * {@inheritDoc}
         *
         * @param item
         * @param empty
         */
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            ContextMenu contextMenu = new ContextMenu();

            // Add a ContextMenu if this is a TreeTableRootItem
            TreeItem<AccountingEntity> treeItem = getTreeTableRow().getTreeItem();
            if (treeItem instanceof TreeTableRootItem) {
                MenuItem addMenuItem = new MenuItem();

                if (treeItem == inmatesRoot) {
                    addMenuItem.setText("Huischgenoot toevoegen");
                } else if (treeItem == assetsRoot) {
                    addMenuItem.setText("Asset toevoegen");
                } else if (treeItem == expensesRoot) {
                    addMenuItem.setText("Expense toevoegen");
                } else if (treeItem == liabilitiesRoot) {
                    addMenuItem.setText("Liability toevoegen");
                } else if (treeItem == dividendsRoot) {
                    addMenuItem.setText("Dividend toevoegen");
                } else if (treeItem == revenuesRoot) {
                    addMenuItem.setText("Revenue toevoegen");
                } else if (treeItem == equitiesRoot) {
                    addMenuItem.setText("Equity toevoegen");
                }

                // FIXME implement action
                addMenuItem.setOnAction(event -> {
                    System.err.println("ContextMenuAction not yet implemented");
                });

                contextMenu.getItems().clear();
                contextMenu.getItems().add(addMenuItem);
            }

            setContextMenu(contextMenu);
//            AccountingEntityTreeTableView.this.setContextMenu(contextMenu);
        }
    }

}
