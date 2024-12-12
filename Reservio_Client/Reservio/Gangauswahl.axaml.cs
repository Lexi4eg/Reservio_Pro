using System;
using System.Collections.Generic;
using System.Linq;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class GangPage : UserControl
    {
        private string Personenanzahl { get; }
        private DateTime Datum { get; }

        // Kapazitäten der Tische
        private readonly Dictionary<string, int> tischKapazitäten = new Dictionary<string, int>
        {
            { "G1", 8 },
            { "G2", 8 }
        };

        public GangPage(string personenanzahl, DateTime datum)
        {
            InitializeComponent();
            int personenanzahl_ = int.Parse(personenanzahl);
            Datum = datum;
            Personenanzahl = personenanzahl;
            
            var erlaubteTische = tischKapazitäten
                .Where(kv => kv.Value >= personenanzahl_)
                .Select(kv => kv.Key);

            foreach (var tisch in erlaubteTische)
            {
                areaComboBox.Items.Add(new ComboBoxItem { Content = tisch });
            }
        }

        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            if (areaComboBox.SelectedItem != null)
            {
                string selectedTable = (areaComboBox.SelectedItem as ComboBoxItem)?.Content?.ToString();

                this.Content = new Personendaten(selectedTable, Personenanzahl, Datum);
            }
            else
            {
                ShowErrorMessage("Bitte wählen Sie einen Tisch aus.");
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

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new ThirdPage(Personenanzahl, Datum);
        }
    }
}
