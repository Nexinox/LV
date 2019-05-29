package de.hofmann.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.datacess.ProductClient;
import de.hofmann.modell.Product;

import java.util.List;

@PageTitle("Liste Aller Produkte")
@Route("")
public class ListProductView extends Div {
	private static final long serialVersionUID = 1L;

	private ProductClient client;

	public ListProductView() {

		client = new ProductClient();
		Grid<Product> grid = new Grid<>();
		Button create = new Button("Neuer eintrag");


		List<Product> products = client.getAllProducts();

		Label Title = new Label("Liste Aller Produkte");



		grid.setItems(products);

		grid.addColumn(Product::getName).setHeader("Name");
		grid.addColumn(Product::getAdDate).setHeader("Kaufdatum");
		grid.addColumn(Product::getDuration).setHeader("Verfalsdatum");
		grid.addComponentColumn(this::buildQuantButtons).setHeader("Menge");
		grid.addComponentColumn(this::buildDeleteButton).setHeader("löschen");
		grid.setHeightByRows(true);
		grid.setSizeFull();



		HorizontalLayout hlayout = new HorizontalLayout(grid);
		hlayout.setSizeFull();

		create.addClickListener(e->this.getUI().ifPresent(ui->ui.navigate(CreateProductView.class)));

		grid.addSelectionListener(s-> this.getUI().ifPresent(ui-> ui.navigate(EditProductView.class, s.getFirstSelectedItem().get().getID())));

		VerticalLayout layout = new VerticalLayout(Title, hlayout, create);
		this.add(layout);
	}

	private HorizontalLayout buildQuantButtons(Product product) {

		Label qunt = new Label(Integer.toString(product.getQuantity()));

		Button add = new Button("+", e->{
			client.plusQuant(product);
			qunt.setText(Integer.toString(Integer.parseInt(qunt.getText())+1));
		});

		Button min = new Button("-", e->{
			client.minQuant(product);
			qunt.setText(Integer.toString(Integer.parseInt(qunt.getText())-1));
		});

		return new HorizontalLayout(qunt, add, min);
	}

	private Button buildDeleteButton(Product p) {
		return new Button("DEL", e -> {client.deleteProduct(p);UI.getCurrent().getPage().reload();});
	}

}