using System;
using System.Runtime.InteropServices.JavaScript;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class Reservierungsdaten : UserControl
    {
        public Reservierungsdaten()
        {
            InitializeComponent();
            InitializeHours();
            InitializeMinutes();
        }

        private void InitializeHours()
        {

            // Startwert für die Stunden
            for (int stunde = 10; stunde < 22; stunde++) // Füge insgesamt 22 Stunden hinzu
            {
                hoursComboBox.Items.Add(stunde);
            }
        }

        private void InitializeMinutes()
        {
            int minute = 0;
            while (minute < 60)
            {
                minutesComboBox.Items.Add(minute);
                minute = minute + 15;

            }
        }

        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            if (!IsInputValid())
            {
                ShowErrorMessage("Bitte füllen Sie alle Felder aus, bevor Sie fortfahren!");
                return;
            }

            string personenanzahl = Personenanzahl.Text;
            var selectedDate = datePicker.SelectedDate.HasValue
                ? datePicker.SelectedDate.Value.DateTime
                : DateTime.MinValue;
            var selectedHour = hoursComboBox.SelectedItem.ToString();
            var selectedMinute = minutesComboBox.SelectedItem.ToString();
            
            var time = TimeSpan.Parse($"{selectedHour}:{selectedMinute}");
            var reservierungsDatum = selectedDate.Date + time;
            
            this.Content = new Bereichsauswahl(personenanzahl, reservierungsDatum); // nächste Seite aufrufen
        }

        private bool IsInputValid()
        {
            // überprüft, ob alle Daten ausgefüllt wurden, wenn nicht dann Fehlermeldung
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

        private void DatePicker_OnSelectedDateChanged(object? sender,
            DatePickerSelectedValueChangedEventArgs datePickerSelectedValueChangedEventArgs)
        {
            var datePicker = sender as DatePicker;
            if (datePicker?.SelectedDate != null)
            {
                DateTimeOffset selectedDate = datePicker.SelectedDate.Value;

                // Überprüfen, ob das Datum in der Vergangenheit liegt
                if (selectedDate < DateTime.Now.Date)
                {
                    // Setzt das Datum auf den heutigen Tag zurück & zeigt Fehlermeldung
                    datePicker.SelectedDate = DateTime.Now.Date;
                    datePicker.SelectedDate = null;
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