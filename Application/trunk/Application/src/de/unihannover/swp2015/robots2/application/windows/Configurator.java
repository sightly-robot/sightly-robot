package de.unihannover.swp2015.robots2.application.windows;

import java.net.URL;
import java.util.Locale;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.Vote;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.Button.State;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.ButtonStateListener;
import org.apache.pivot.wtk.Checkbox;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentMouseButtonListener;
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
import org.apache.pivot.wtk.TextInputContentListener;
import org.apache.pivot.wtk.Window;
import org.apache.pivot.wtk.validation.DoubleRangeValidator;
import org.apache.pivot.wtk.validation.IntRangeValidator;
import org.apache.pivot.wtk.validation.IntValidator;

import de.unihannover.swp2015.robots2.application.models.GeneralOptions;
import de.unihannover.swp2015.robots2.application.models.VisualizationRegister;
import de.unihannover.swp2015.robots2.application.util.CheckboxStateConverter;
import de.unihannover.swp2015.robots2.controller.externalInterfaces.IVisualizationControl;
import de.unihannover.swp2015.robots2.controller.main.GuiMainController;
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
	@BXML private Checkbox showBubble;
	@BXML private Checkbox showVirtualBubble;
	@BXML private Checkbox showLockStates;
	
	@BXML private PushButton nextTexturepack;
	
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
		
		reinstateSliderListeners();

		xOffset.getComponentMouseButtonListeners().add(finalChangeCommitAction);
		yOffset.getComponentMouseButtonListeners().add(finalChangeCommitAction);
		xScale.getComponentMouseButtonListeners().add(finalChangeCommitAction);
		yScale.getComponentMouseButtonListeners().add(finalChangeCommitAction);
		transformation.getComponentMouseButtonListeners().add(finalChangeCommitAction);
		
		// Validation:
		xOffsetValue.setValidator(new IntRangeValidator(Locale.US, xOffset.getStart(), xOffset.getEnd()));
		yOffsetValue.setValidator(new IntRangeValidator(Locale.US, yOffset.getStart(), yOffset.getEnd()));
		xScaleValue.setValidator(new DoubleRangeValidator(Locale.US, 0.001, 1.));
		yScaleValue.setValidator(new DoubleRangeValidator(Locale.US, 0.001, 1.));
		transformationValue.setValidator(new DoubleRangeValidator(Locale.US, -1., 1.));
		
		xOffsetValue.getTextInputContentListeners().add(TextInputValueListener);
		yOffsetValue.getTextInputContentListeners().add(TextInputValueListener);
		xScaleValue.getTextInputContentListeners().add(TextInputValueListener);
		yScaleValue.getTextInputContentListeners().add(TextInputValueListener);
		transformationValue.getTextInputContentListeners().add(TextInputValueListener);

		// must use press listener or computational changes are also sent back to the visualization.
		// which is not desirable.
		showLockStates.getButtonPressListeners().add(showFlagChanged);
		showReals.getButtonPressListeners().add(showFlagChanged);
		showVirtuals.getButtonPressListeners().add(showFlagChanged);
		showWalls.getButtonPressListeners().add(showFlagChanged);
		showBubble.getButtonPressListeners().add(showFlagChanged);
		showVirtualBubble.getButtonPressListeners().add(showFlagChanged);
		showResources.getButtonPressListeners().add(showFlagChanged);
		
		nextTexturepack.getButtonPressListeners().add(nextTexturepackAction);
	}
	
	/**
	 * Utilitarian function. Gets scale of slider (which only can have integer values, which
	 * then have to be divided to become the proper double value).
	 * @return
	 */
	private float getTransformationSliderResolution() {
		return Math.abs((float)transformation.getStart()) + (float)transformation.getEnd();
	}
	
	/**
	 * xOffsetValueChanged, says it all.
	 */
	private TextInputContentListener.Adapter TextInputValueListener = new TextInputContentListener.Adapter() {
		@Override
		public void textInserted(TextInput textInput, int index, int count) {
			if (!textInput.isTextValid())
				return;
			
			decomissionSliderListeners();
			float value = Float.parseFloat(textInput.getText());
			
			if (textInput == xOffsetValue) {
				xOffset.setValue((int)(value));
				applySliderChange(xOffset);
			}
			else if (textInput == yOffsetValue) {
				yOffset.setValue((int)(value));
				applySliderChange(yOffset);
			}
			else if (textInput == xScaleValue) {
				xScale.setValue((int)(value * xScale.getEnd()));
				applySliderChange(xScale);
			}
			else if (textInput == yScaleValue) {
				yScale.setValue((int)(value * yScale.getEnd()));
				applySliderChange(yScale);	
			}
			else if (textInput == transformationValue) {
				transformation.setValue((int)(value * getTransformationSliderResolution()));
				applySliderChange(transformation);
			}
			
			reinstateSliderListeners();
		}
	};
	
	/**
	 * Applies the changes to the models for the given slider.
	 * @param slider
	 */
	private void applySliderChange(Slider slider) {
		VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
		if (slider == xOffset) {
			temp.setAbscissaOffset((float)xOffset.getValue());			
		} else if (slider == yOffset) {
			temp.setOrdinateOffset((float)yOffset.getValue());			
		} else if (slider == xScale) {
			temp.setAbscissaScale((float)xScale.getValue() / (float)xScale.getEnd());			
		} else if (slider == yScale) {
			temp.setOrdinateScale((float)yScale.getValue() / (float)yScale.getEnd());			
		} else if (slider == transformation) {
			temp.setPerspectiveTransformation((float)transformation.getValue() / getTransformationSliderResolution());			
		}
		visualizations.performMergeOnSelected(temp);
	}
	
	/**
	 * Disables slider listeners.
	 */
	private void decomissionSliderListeners() {
		xOffset.getSliderValueListeners().remove(xOffsetChanged);
		yOffset.getSliderValueListeners().remove(yOffsetChanged);
		xScale.getSliderValueListeners().remove(xScaleChanged);
		yScale.getSliderValueListeners().remove(yScaleChanged);
		transformation.getSliderValueListeners().remove(transformationChanged);
	}
	
	/**
	 * Enables slider listeners.
	 */
	private void reinstateSliderListeners() {
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
	 * Action gets evoked whenever one of the checkboxes change state by user 
	 * interaction.
	 */
	private ButtonPressListener showFlagChanged = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
			temp.setRenderLockState(CheckboxStateConverter.isChecked(showLockStates));
			temp.setRenderRobots(CheckboxStateConverter.isChecked(showReals));
			temp.setRenderVirtualRobots(CheckboxStateConverter.isChecked(showVirtuals));
			temp.setRenderResources(CheckboxStateConverter.isChecked(showResources));
			temp.setRenderBubble(CheckboxStateConverter.isChecked(showBubble));
			temp.setRenderVirtualBubble(CheckboxStateConverter.isChecked(showVirtualBubble));
			temp.setRenderWalls(CheckboxStateConverter.isChecked(showWalls));
			visualizations.performMergeOnSelected(temp);
		}		
	};
	
	/**
	 * Resets the throttling flag, so updates can be performed again.
	 */
	private void issueThrottleReset() {
		ApplicationContext.scheduleCallback(new Runnable() {
			@Override
			public void run() {
				throttleBlock = false;
				
				// final commit
				ApplicationContext.scheduleCallback(new Runnable() {
					@Override
					public void run() {
						if (throttleBlock)
							return;
						finalChangeCommit();
					}					
				}, 150);
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
			
			applySliderChange(xOffset);
			xOffsetValue.setText(new Integer(value).toString());
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
			
			applySliderChange(yOffset);
			yOffsetValue.setText(new Integer(value).toString());
		}		
	};
	
	/**
	 * Automatic updater for xScale slider.
	 */
	private SliderValueListener xScaleChanged = new SliderValueListener() {
		@Override
		public void valueChanged(Slider slider, int value) {
			if (throttleBlock)
				return;
			throttle();
			
			applySliderChange(xScale);
			xScaleValue.setText(new Float(value / (float)xScale.getEnd()).toString());
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

			applySliderChange(yScale);
			yScaleValue.setText(new Float(value / (float)yScale.getEnd()).toString());
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
			
			transformationValue.setText(new Float(value / getTransformationSliderResolution()).toString());
		}		
	};

	/**
	 * This is used to bypass the throttler and commit the last change made when releasing a slider.
	 */
	private ComponentMouseButtonListener finalChangeCommitAction = new ComponentMouseButtonListener() {

		@Override
		public boolean mouseClick(Component slider, org.apache.pivot.wtk.Mouse.Button button, int x, int y,
				int count) {
			// not used
			return false;
		}

		@Override
		public boolean mouseDown(Component slider, org.apache.pivot.wtk.Mouse.Button button, int x, int y) {
			// not used
			return false;
		}

		@Override
		public boolean mouseUp(Component slider, org.apache.pivot.wtk.Mouse.Button button, int x, int y) {
			finalChangeCommit();
			return false;
		}
		
	};
	
	/**
	 * Sends the last changes to the visualization. (Needed because of throttling)
	 * And also applies the slider values to the text boxes.
	 * @param sendToVisualization Do not send to client
	 */
	private void finalChangeCommit(boolean sendToVisualization) {
		VisualizationOptions temp = new VisualizationOptions(visualizations.getSelected().getId());
		temp.setAbscissaOffset(xOffset.getValue());
		temp.setOrdinateOffset(yOffset.getValue());
		temp.setAbscissaScale(xScale.getValue() / (float)xScale.getEnd());
		temp.setOrdinateScale(yScale.getValue() / (float)yScale.getEnd());
		temp.setPerspectiveTransformation(transformation.getValue() / getTransformationSliderResolution());
		
		visualizations.performMergeOnSelected(temp);

		xOffsetValue.setText(new Integer(xOffset.getValue()).toString());
		yOffsetValue.setText(new Integer(yOffset.getValue()).toString());
		xScaleValue.setText(new Float(xScale.getValue() / (float)xScale.getEnd()).toString());
		yScaleValue.setText(new Float(yScale.getValue() / (float)yScale.getEnd()).toString());
		transformationValue.setText(new Float(transformation.getValue() / getTransformationSliderResolution()).toString());
	}
	
	/**
	 * Sends the last changes to the visualization. (Needed because of throttling).
	 * And also applies the slider values to the text boxes.
	 */
	private void finalChangeCommit() {
		finalChangeCommit(true);
	}

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
		decomissionSliderListeners();
		
		VisualizationOptions visu = visualizations.getSelected();
		visu.leftMerge(VisualizationOptions.createDefault(""));

		xOffset.setValue(visu.getAbscissaOffset().get().intValue());
		yOffset.setValue(visu.getOrdinateOffset().get().intValue());
		xScale.setValue(new Float(visu.getAbscissaScale().get() * xScale.getEnd()).intValue());
		yScale.setValue(new Float(visu.getOrdinateScale().get() * yScale.getEnd()).intValue());
		transformation.setValue(new Float(visu.getPerspectiveTransformation().get() * getTransformationSliderResolution()).intValue());
		
		CheckboxStateConverter.setChecked(showWalls, visu.doesRenderWalls().get());
		CheckboxStateConverter.setChecked(showVirtuals, visu.doesRenderVirtualRobots().get());
		CheckboxStateConverter.setChecked(showReals, visu.doesRenderRobots().get());
		CheckboxStateConverter.setChecked(showResources, visu.doesRenderResources().get());
		CheckboxStateConverter.setChecked(showBubble, visu.doesRenderBubble().get());
		CheckboxStateConverter.setChecked(showVirtualBubble, visu.doesRenderVirtualBubble().get());
		CheckboxStateConverter.setChecked(showLockStates, visu.doesRenderLockStates().get());
		
		finalChangeCommit(false);
		reinstateSliderListeners();
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
		showVirtualBubble.setEnabled(true);
		showBubble.setEnabled(true);
		showLockStates.setEnabled(true);
		
		nextTexturepack.setEnabled(true);
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


	private ButtonPressListener nextTexturepackAction = new ButtonPressListener() {
		@Override
		public void buttonPressed(Button arg0) {
			visualizations.cycleTexturepackForSelected();
		}
	};
}