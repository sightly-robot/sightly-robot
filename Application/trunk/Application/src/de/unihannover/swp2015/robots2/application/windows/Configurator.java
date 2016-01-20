package de.unihannover.swp2015.robots2.application.windows;

import java.awt.Color;
import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ButtonStateListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.ListButtonSelectionListener;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Slider;
import org.apache.pivot.wtk.SliderValueListener;
import org.apache.pivot.wtk.TabPane;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextArea.Paragraph;
import org.apache.pivot.wtk.TextAreaContentListener;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.Button.State;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;
import de.unihannover.swp2015.robots2.application.models.TableElement;
import de.unihannover.swp2015.robots2.application.models.VisualizationRegister;
import de.unihannover.swp2015.robots2.application.util.CheckboxStateConverter;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
import de.unihannover.swp2015.robots2.model.externalInterfaces.IModelObserver;
import de.unihannover.swp2015.robots2.model.interfaces.IEvent;
import de.unihannover.swp2015.robots2.utils.VisualizationOptions;

/**
 * Controller for Configuration Window.
 * @author Tim Ebbeke
 */
public class Configurator extends Window implements Bindable, IVisualizationControl
{
	private final long throttleDelay = 100; // ms
	
	// BXML bindings (General Options)
	@BXML private TabPane tabs;
	@BXML private TextArea ipTextArea;
	@BXML private Checkbox debugMode;
	
	// BXML bindings (Visualization Options)
	@BXML private ListButton visualizationList;
	@BXML private PushButton updateButton;
	
	// (Visualization Options) xOffset
	@BXML private Label xOffsetLabel;
	@BXML private Slider xOffset;
	@BXML private TextInput xOffsetValue;
	
	// (Visualization Options) yOffset
	@BXML private Label yOffsetLabel;
	@BXML private Slider yOffset;
	@BXML private TextInput yOffsetValue;
	
	// (Visualization Options) xScale
	@BXML private Label xScaleLabel;
	@BXML private Slider xScale;
	@BXML private TextInput xScaleValue;
	
	// (Visualization Options) yScale
	@BXML private Label yScaleLabel;
	@BXML private Slider yScale;
	@BXML private TextInput yScaleValue;
	
	// (Visualization Options) transformation
	@BXML private Label transformationLabel;
	@BXML private Slider transformation;
	@BXML private TextInput transformationValue;
	
	// (Visualization Options) flags
	@BXML private Checkbox showWalls;
	@BXML private Checkbox showVirtuals;
	@BXML private Checkbox showReals;
	@BXML private Checkbox showResources;
	@BXML private Checkbox showScore;
	@BXML private Checkbox showName;
	
	@BXML private PushButton applyButton;
	
	private volatile boolean throttleBlock;
	
	// Models
	GeneralOptions generalOptions;
	GuiMainController controller;
	VisualizationRegister visualizations;
	
	/**
	 * Apache pivot initialize method. Called on initialization of window. 
	 */
	@Override
	public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
		generalOptions = new GeneralOptions();
		
		setGeneralOptions(generalOptions); // set to defaults
		
		ipTextArea.getTextAreaContentListeners().add(ipChangeAction);
		debugMode.getButtonStateListeners().add(debugModeChangeAction);
		updateButton.getButtonPressListeners().add(updateVisualizationListAction);
		visualizationList.getListButtonSelectionListeners().add(visualizationChangeAction);
		
		xOffset.getSliderValueListeners().add(xOffsetChanged);
		yOffset.getSliderValueListeners().add(yOffsetChanged);
		xScale.getSliderValueListeners().add(xScaleChanged);
		yScale.getSliderValueListeners().add(yScaleChanged);
		transformation.getSliderValueListeners().add(transformationChanged);
	}
	
	/**
	 * Sets the throttle flag and issues the reset.
	 */
	private void throttle() {
		if (!throttleBlock) {
			throttleBlock = true;
			issueThrottleReset();
		}
	}
	
	/**
	 * Resets the throttling flag, so updates can be performed again.
	 */
	private void issueThrottleReset() {
		ApplicationContext.scheduleCallback(new Runnable() {
			@Override
			public void run() {
				throttleBlock = false;				
			}
		}, throttleDelay);
	}

	/**
	 * Automatic updater for xOffset slider.
	 */
	private SliderValueListener xOffsetChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider arg0, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			xOffsetValue.setText(new Integer(value).toString());
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setAbscissaOffset((float)value);
			visualizations.performMergeOnSelected(temp);
		}		
	};
	
	/**
	 * Automatic updater for yOffset slider.
	 */
	private SliderValueListener yOffsetChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider arg0, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			yOffsetValue.setText(new Integer(value).toString());
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setOrdinateOffset((float)value);
			visualizations.performMergeOnSelected(temp);
		}		
	};
	
	/**
	 * Automatic updater for xScale slider.
	 */
	private SliderValueListener xScaleChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider arg0, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			xScaleValue.setText(new Integer(value).toString());
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setAbscissaScale((float)value / 10000.f);
			visualizations.performMergeOnSelected(temp);
		}		
	};
	
	/**
	 * Automatic updater for yScale slider.
	 */
	private SliderValueListener yScaleChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider arg0, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			yScaleValue.setText(new Integer(value).toString());
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setOrdinateScale((float)value / 10000.f);
			visualizations.performMergeOnSelected(temp);
		}		
	};

	/**
	 * Automatic updater for transformation slider.
	 */
	private SliderValueListener transformationChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider arg0, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			transformationValue.setText(new Integer(value).toString());
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setPerspectiveTransformation((float)value / 10000.f);
			visualizations.performMergeOnSelected(temp);
		}		
	};

	/**
	 * Button to update the visualization list.
	 */
	private ButtonPressListener updateVisualizationListAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button button) {
			visualizations.clear();
			controller.getVisualizationSettings();
		}
	};
	
	/**
	 * A new visualization was selected to be changed.
	 */
	private ListButtonSelectionListener visualizationChangeAction = new ListButtonSelectionListener() {

		@Override
		public void selectedIndexChanged(ListButton listButton, int index) { /* not used */ }

		@Override
		public void selectedItemChanged(ListButton listButton, Object item) { 
			if (listButton.getSelectedItem() != null) {
				visualizations.setSelection((String)listButton.getSelectedItem());
				loadSelectedVisualization();
				enableVisualizationSettingsControls();
			}
		}		
	};
	
	/**
	 * Loads the selected visualization.
	 */
	private void loadSelectedVisualization() {
		VisualizationOptions visu = visualizations.getSelected();
		
		xOffset.setValue(visu.getAbscissaOffset().get().intValue());
		yOffset.setValue(visu.getOrdinateOffset().get().intValue());
		xScale.setValue(new Float(visu.getAbscissaScale().get() * 10000.).intValue());
		yScale.setValue(new Float(visu.getOrdinateScale().get() * 10000.).intValue());
		transformation.setValue(new Float(visu.getPerspectiveTransformation().get() * 1000.).intValue());
		
		CheckboxStateConverter.setChecked(showWalls, visu.doesRenderWalls().get());
		CheckboxStateConverter.setChecked(showVirtuals, visu.doesRenderVirtualRobots().get());
		CheckboxStateConverter.setChecked(showReals, visu.doesRenderRobots().get());
		CheckboxStateConverter.setChecked(showResources, visu.doesRenderResources().get());
		CheckboxStateConverter.setChecked(showScore, visu.doesRenderScore().get());
		CheckboxStateConverter.setChecked(showName, visu.doesRenderName().get());
	}
	
	/** 
	 * Enables all visualization settings controls.
	 */
	private void enableVisualizationSettingsControls() {
		xOffsetLabel.setEnabled(true);
		xOffset.setEnabled(true);
		xOffsetValue.setEnabled(true);

		yOffsetLabel.setEnabled(true);
		yOffset.setEnabled(true);
		yOffsetValue.setEnabled(true);
		
		xScaleLabel.setEnabled(true);
		xScale.setEnabled(true);
		xScaleValue.setEnabled(true);

		yScaleLabel.setEnabled(true);
		yScale.setEnabled(true);
		yScaleValue.setEnabled(true);
		
		transformationLabel.setEnabled(true);
		transformation.setEnabled(true);
		transformationValue.setEnabled(true);
		
		showWalls.setEnabled(true);
		showVirtuals.setEnabled(true);
		showReals.setEnabled(true);
		showResources.setEnabled(true);
		showScore.setEnabled(true);
		showName.setEnabled(true);
		
		//applyButton.setEnabled(true);
	}

	/**
	 * Get generalOptions.
	 * @return Returns all generalOptions. 
	 */
	public GeneralOptions getGeneralOptions() {
		return generalOptions;
	}

	/**
	 * Set generalOptions.
	 * @param generalOptions see GeneralOptions class.
	 */
	public void setGeneralOptions(GeneralOptions generalOptions) {
		this.generalOptions = generalOptions;
		ipTextArea.setText(generalOptions.getRemoteUrl());
		
		if (generalOptions.isInDebugMode())
			debugMode.setState(State.SELECTED);
		else
			debugMode.setState(State.UNSELECTED);
	}	
	
	/**
	 * Updates generalOptions on changing ip text immediately.
	 */
	private TextAreaContentListener ipChangeAction = new TextAreaContentListener() {
		@Override
		public void paragraphInserted(TextArea self, int arg1) {}

		@Override
		public void paragraphsRemoved(TextArea self, int arg1, Sequence<Paragraph> arg2) {}

		@Override
		public void textChanged(TextArea self) {
			generalOptions.setRemoteUrl(self.getText());
		}		
	};


	/**
	 * Listener for debugMode checkbox.
	 */
	private ButtonStateListener debugModeChangeAction = new ButtonStateListener() {
		@Override
		public void stateChanged(Button arg0, State state) {
			generalOptions.setInDebugMode(!state.equals(State.SELECTED));
		}
	};

	/**
	 * Set GuiMainController for internal use.
	 * @param controller THE GUI controller.
	 */
	void setController(GuiMainController controller) {
		this.controller = controller;
		controller.registerVisualizationControl(this);
		updateButton.setEnabled(true);
		visualizations = new VisualizationRegister(controller);
	}

	/**
	 * This is called whenever a request for all visualization settings is requested.
	 * For each and every visualization.
	 */
	@Override
	public void receiveSettings(String settings) {
		// TODO Auto-generated method stub
		VisualizationOptions visu = VisualizationOptions.createFromJson(settings);
		visualizations.addVisualization(visu.getId(), visu);
		
		ApplicationContext.queueCallback(new Runnable() {			
			@Override
			public void run() {
				visualizationList.setListData(visualizations.getIdList());
			}
		});
	}
}