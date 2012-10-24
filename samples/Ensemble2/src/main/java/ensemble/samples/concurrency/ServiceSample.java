/*
 * Copyright (c) 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ensemble.samples.concurrency;

import ensemble.Sample;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A sample showing use of a Service to retrieve data in a background thread.
 * Selecting the Refresh button restarts the Service.
 *
 * @see javafx.collections.FXCollections
 * @see javafx.concurrent.Service
 * @see javafx.concurrent.Task
 * @see javafx.scene.control.ProgressIndicator
 * @see javafx.scene.control.TableColumn
 * @see javafx.scene.control.TableView
 */
public class ServiceSample extends Sample {

    final GetDailySalesService service = new GetDailySalesService();

    public ServiceSample() {

        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(12));
        TableView tableView = new TableView();
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                service.restart();
            }
        });
        vbox.getChildren().addAll(tableView, button);

        Region veil = new Region();
        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4)");
        ProgressIndicator p = new ProgressIndicator();
        p.setMaxSize(150, 150);

        //Define table columns
        TableColumn idCol = new TableColumn();
        idCol.setText("ID");
        idCol.setCellValueFactory(new PropertyValueFactory("dailySalesId"));
        tableView.getColumns().add(idCol);
        TableColumn qtyCol = new TableColumn();
        qtyCol.setText("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory("quantity"));
        tableView.getColumns().add(qtyCol);
        TableColumn dateCol = new TableColumn();
        dateCol.setText("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        dateCol.setMinWidth(240);
        tableView.getColumns().add(dateCol);


        p.progressProperty().bind(service.progressProperty());
        veil.visibleProperty().bind(service.runningProperty());
        p.visibleProperty().bind(service.runningProperty());
        tableView.itemsProperty().bind(service.valueProperty());

        StackPane stack = new StackPane();
        stack.getChildren().addAll(vbox, veil, p);

        getChildren().add(stack);
        service.start();
    }

    /**
     * A service for getting the DailySales data. This service exposes an
     * ObservableList for convenience when using the service. This
     * <code>results</code> list is final, though its contents are replaced when
     * a service call successfully concludes.
     */
    public class GetDailySalesService extends Service<ObservableList<DailySales>> {

        /**
         * Create and return the task for fetching the data. Note that this
         * method is called on the background thread (all other code in this
         * application is on the JavaFX Application Thread!).
         *
         * @return A task
         */
        @Override
        protected Task createTask() {
            return new GetDailySalesTask();
        }
    }

    public class GetDailySalesTask extends Task<ObservableList<DailySales>> {       
        @Override protected ObservableList<DailySales> call() throws Exception {
            for (int i = 0; i < 500; i++) {
                updateProgress(i, 500);
                Thread.sleep(5);
            }
            ObservableList<DailySales> sales = FXCollections.observableArrayList();
            sales.add(new DailySales(1, 5000, new Date()));
            sales.add(new DailySales(2, 2473, new Date(0)));
            return sales;
        }
    }

    public class DailySales {

        private Integer dailySalesId;
        private Integer quantity;
        private Date date;

        public DailySales() {
        }

        public DailySales(int id, int qty, Date date) {
            this.dailySalesId = id;
            this.quantity = qty;
            this.date = date;
        }

        public Integer getDailySalesId() {
            return dailySalesId;
        }

        public void setDailySalesId(Integer dailySalesId) {
            this.dailySalesId = dailySalesId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
