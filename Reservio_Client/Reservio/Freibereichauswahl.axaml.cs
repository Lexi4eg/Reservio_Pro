using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class FreibereichPage : UserControl
    {
        private string Personenanzahl { get; }
        private DateTime Datum { get; }

        // Kapazitäten der Tische
        private readonly Dictionary<string, int> tischKapazitäten = new Dictionary<string, int>
        {
            { "F1", 2 },
            { "F2", 4 },
            { "F3", 2 },
            { "F4", 4 },
            { "F5", 2 },
            { "F6", 4 },
            { "F7", 2 },
            { "F8", 4 },
            { "F9", 2 },
            { "F10", 2 }
        };

        public FreibereichPage(string personenanzahl, DateTime datum)
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

                this.Content = new Kontaktdaten(selectedTable, Personenanzahl, Datum);
            }
            else
            {
                Fehlermeldung.Text = "Bitte wählen Sie einen Tisch aus.";
            }
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new Bereichsauswahl(Personenanzahl, Datum);
        }
    }
}
