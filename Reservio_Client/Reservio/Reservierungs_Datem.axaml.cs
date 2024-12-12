using System;
using System.Runtime.InteropServices.JavaScript;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class SecondPage : UserControl
    {
        public SecondPage()
        {
            InitializeComponent();
        }

        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            if (!IsInputValid())
            {
                ShowErrorMessage("Bitte füllen Sie alle Felder aus, bevor Sie fortfahren!");
                return;
            }

            string personenanzahl = Personenanzahl.Text;
            var selectedDate = datePicker.SelectedDate.HasValue ? datePicker.SelectedDate.Value.DateTime : DateTime.MinValue;
            var selectedHour = (hoursComboBox.SelectedItem as ComboBoxItem)?.Content.ToString();
            var selectedMinute = (minutesComboBox.SelectedItem as ComboBoxItem)?.Content.ToString();

            var time = TimeSpan.Parse($"{selectedHour}:{selectedMinute}");
            var reservierungsDatum = selectedDate.Date + time;

            this.Content = new ThirdPage(personenanzahl, reservierungsDatum);
        }

        private bool IsInputValid()
        {
            if (!datePicker.SelectedDate.HasValue)
                return false;

            if (hoursComboBox.SelectedItem == null)
                return false;

            if (minutesComboBox.SelectedItem == null)
                return false;

            if (string.IsNullOrWhiteSpace(Personenanzahl.Text))
                return false;

            return true;
        }
        
        private void DatePicker_OnSelectedDateChanged(object? sender, DatePickerSelectedValueChangedEventArgs datePickerSelectedValueChangedEventArgs)
        {
            var datePicker = sender as DatePicker;
            if (datePicker?.SelectedDate != null)
            {
                DateTimeOffset selectedDate = datePicker.SelectedDate.Value;

                // Überprüfen, ob das Datum in der Vergangenheit liegt
                if (selectedDate < DateTime.Now.Date)
                {
                    // Setze das Datum auf den heutigen Tag zurück oder zeige eine Fehlermeldung
                    datePicker.SelectedDate = DateTime.Now.Date;
                    ShowErrorMessage("Bitte wählen Sie ein zukünftiges Datum aus.");
                }
            }
        }

        private void ShowErrorMessage(string message)
        {
            var errorWindow = new Window
            {
                Width = 300,
                Height = 150,
                Content = new TextBlock
                {
                    Text = message,
                    HorizontalAlignment = Avalonia.Layout.HorizontalAlignment.Center,
                    VerticalAlignment = Avalonia.Layout.VerticalAlignment.Center,
                    TextWrapping = Avalonia.Media.TextWrapping.Wrap,
                    FontSize = 16
                }
            };

            errorWindow.ShowDialog((Window)this.VisualRoot);
        }

        private void OnHauptseiteClick(object? sender, RoutedEventArgs e)
        {
            throw new NotImplementedException();
        }
    }
}
