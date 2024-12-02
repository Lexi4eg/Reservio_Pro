using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class ThirdPage : UserControl
    {
        private string Personenanzahl { get; }

        public ThirdPage(string personenanzahl)
        {
            InitializeComponent();
            Personenanzahl = personenanzahl;
        }
        
        // Event handler for Weiter button click
        private void OnWeiterButtonClick2(object sender, RoutedEventArgs e)
        {
            // Get the selected item from the ComboBox
            var selectedItem = this.FindControl<ComboBox>("areaComboBox").SelectedItem as ComboBoxItem;
            

            if (selectedItem != null)
            {
                string selectedArea = selectedItem.Content.ToString();

                // Check if the selected area is "Lounge"
                if (selectedArea == "Lounge")
                {
                    // Navigate to the LoungePage
                    this.Content = new LoungePage(Personenanzahl);
                }
                else if (selectedArea == "Gang")
                {
                    this.Content = new GangPage(Personenanzahl);
                }
                else if (selectedArea == "Saal")
                {
                    this.Content = new SaalPage(Personenanzahl);
                }
                else if (selectedArea == "Terrasse")
                {
                    this.Content = new TerrassenPage(Personenanzahl);
                }
                else if (selectedArea == "Freibereich")
                {
                    this.Content = new FreibereichPage(Personenanzahl);
                }
            }
        }
        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            // Lade die neue Seite in das ContentControl (hier kannst du eine andere Seite laden)
            this.Content = new SecondPage();  // `SecondPage` muss ein UserControl sein
        }
    }
}