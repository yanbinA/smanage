package com.temple.manage.PoiUtils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.SchemaType;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 * ScatterChart
 * </p>
 *
 * @author messi
 * @package com.temple.manage.PoiUtils
 * @description ScatterChart
 * @date 2021-12-20 0:13
 * @verison V1.0.0
 */
public class ScatterChart {
    private ScatterChart() {}

    public static void main(String[] args) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = wb.createSheet("Sheet 1");
            final int NUM_OF_ROWS = 5;
            final int NUM_OF_COLUMNS = 10;

            // Create a row and put some cells in it. Rows are 0 based.
            Row row;
            Cell cell;
            for (int rowIndex = 0; rowIndex < NUM_OF_ROWS; rowIndex++) {
                row = sheet.createRow((short) rowIndex);
                for (int colIndex = 0; colIndex < NUM_OF_COLUMNS; colIndex++) {
                    cell = row.createCell((short) colIndex);
                    cell.setCellValue(colIndex * (rowIndex + 1.0) % 10);
                }
            }

            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 10, 25);

            XSSFChart chart = drawing.createChart(anchor);
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);
            XDDFValueAxis bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("x"); // https://stackoverflow.com/questions/32010765
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setTitle("f(x)");
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

            XDDFDataSource<Double> xs = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(0, 0, 0, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys1 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys2 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys3 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(3, 3, 1, NUM_OF_COLUMNS - 1));
            XDDFNumericalDataSource<Double> ys4 = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(4, 4, 1, NUM_OF_COLUMNS - 1));


            XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
            XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) data.addSeries(xs, ys1);
            series1.setTitle("2x", null); // https://stackoverflow.com/questions/21855842
            series1.setSmooth(false); // https://stackoverflow.com/questions/39636138
            XDDFScatterChartData.Series series2 = (XDDFScatterChartData.Series) data.addSeries(xs, ys2);
            series2.setTitle("3x", null);
            series2.setSmooth(false);
            chart.plot(data);
            XDDFBarChartData barChartData = (XDDFBarChartData) chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            barChartData.setBarDirection(BarDirection.COL);
            barChartData.setVaryColors(Boolean.FALSE);
            XDDFBarChartData.Series series3 = (XDDFBarChartData.Series) barChartData.addSeries(xs, ys3);
            series3.setTitle("3x bar", null);
            series3.setShowLeaderLines(true);
            XDDFChartData.Series series4 = barChartData.addSeries(xs, ys4);
            series4.setTitle("4x bar", null);
            chart.plot(barChartData);
            solidLineSeries(data, 0, PresetColor.CHARTREUSE);
            solidLineSeries(data, 1, PresetColor.TURQUOISE);
            solidLineSeries(barChartData, 0, PresetColor.GREEN);
            solidLineSeries(barChartData, 1, PresetColor.GREEN_YELLOW);

            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            Arrays.stream(plotArea.getBarChartArray(0).getSerArray()).forEach(ser -> {
                ser.addNewDLbls();
                ser.getDLbls().addNewShowVal().setVal(true);
                ser.getDLbls().addNewShowLegendKey().setVal(false);
                ser.getDLbls().addNewShowCatName().setVal(false);
                ser.getDLbls().addNewShowSerName().setVal(false);
            });
            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("ooxml-scatter-chart.xlsx")) {
                wb.write(fileOut);
            }
        }
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFChartData.Series series = data.getSeries(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
//        properties.setLineProperties(line);
//        properties.setFillProperties(fill);
        series.setShapeProperties(properties);
    }
}
